object Main {
  def main(args: Array[String]): Unit = {
    println("Starting Fraud Detection Service...")
    FraudDetectionService.start("fraud-detection-service")
  }
}
