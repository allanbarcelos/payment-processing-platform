import java.util.Properties
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.common.serialization.Serdes

object KafkaConfig {
  def getKafkaConfig(appId: String): Properties = {
    // val props = new Properties()
    // props.put(StreamsConfig.APPLICATION_ID_CONFIG, appId)
    // props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092")
    // props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, "org.apache.kafka.common.serialization.Serdes$StringSerde")
    // props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, "org.apache.kafka.common.serialization.Serdes$StringSerde")
    val props = new Properties();
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "fraud-detection-service");
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    props.put(StreamsConfig.RETRIES_CONFIG, 10); // Add retries
    props.put(StreamsConfig.RETRY_BACKOFF_MS_CONFIG, 1000); // Retry backoff
    props.put(StreamsConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000); // Request timeout
    // props.put("auto.create.topics.enable", "true");

    props
  }
}
