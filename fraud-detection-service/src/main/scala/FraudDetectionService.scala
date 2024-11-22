import io.circe.parser._
import io.circe.generic.auto._
import models.Payment
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.KStream
import org.apache.kafka.streams.KafkaStreams
import java.util.Properties

import org.apache.kafka.streams.scala.serialization.Serdes._
import org.apache.kafka.streams.scala.ImplicitConversions._


import org.apache.kafka.clients.admin.{AdminClient, NewTopic}
import java.util.Collections

object FraudDetectionService {

  def start(appId: String): Unit = {
    val props: Properties = KafkaConfig.getKafkaConfig(appId)

    val adminClient = AdminClient.create(props)

    val requiredTopics = Seq("payment-initiated", "payment-fraud-checked")

    // Verifica e cria os tópicos, se necessário
    requiredTopics.foreach { topic =>
      val topics = adminClient.listTopics().names().get()
      if (!topics.contains(topic)) {
        adminClient.createTopics(Collections.singletonList(new NewTopic(topic, 1, 1.toShort)))
        println(s"Created topic: $topic")
      }
    }

    adminClient.close()

    // val builder = new StreamsBuilder()
    // val paymentsStream: KStream[String, String] = builder.stream[String, String]("payment-initiated")


    val builder = new StreamsBuilder()

    // Consome mensagens do tópico payment-initiated
    val paymentsStream: KStream[String, String] = builder.stream[String, String]("payment-initiated")

    val fraudCheckedStream: KStream[String, String] = paymentsStream.mapValues { value =>
      // Decodifica o JSON para o modelo Payment
      val payment = decode[Payment](value).getOrElse(
        throw new RuntimeException(s"Failed to parse payment: $value")
      )

      // Aplica a lógica de detecção de fraudes
      val isFraud = detectFraud(payment)

      // Marca o pagamento como fraudulento ou válido
      val status = if (isFraud) "fraudulent" else "valid"
      val updatedPayment = payment.copy(status = status)

      // Codifica o pagamento atualizado para JSON
      io.circe.syntax.EncoderOps(updatedPayment).asJson.noSpaces
    }

    // Publica o resultado no tópico payment-fraud-checked
    fraudCheckedStream.to("payment-fraud-checked")

    // Inicia o Kafka Streams
    val streams = new KafkaStreams(builder.build(), props)
    streams.start()

    // Graceful shutdown
    sys.addShutdownHook {
      streams.close()
    }
  }

  def detectFraud(payment: Payment): Boolean = {
    // Exemplo de lógica simples: pagamentos acima de $1000 são marcados como suspeitos
    payment.amount > 1000
  }
}
