document.getElementById("paymentForm").addEventListener("submit", async function (event) {
    event.preventDefault(); // Impede o envio padrão do formulário
  
    const userId = document.getElementById("userId").value;
    const amount = document.getElementById("amount").value;
  
    const paymentData = {
      userId: userId,
      amount: parseFloat(amount),
      status: "initiated", // Status inicial do pagamento
    };
  
    try {
      const response = await fetch("http://localhost/api/payments", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(paymentData),
      });
  
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
  
      const responseData = await response.json();
  
      document.getElementById("responseContainer").innerHTML = `
        <p><strong>Status:</strong> ${responseData.status}</p>
        <p><strong>Message:</strong> ${responseData.message || "Payment processed successfully!"}</p>
      `;
    } catch (error) {
      document.getElementById("responseContainer").innerHTML = `
        <p style="color: red;">Failed to process payment: ${error.message}</p>
      `;
    }
  });
  