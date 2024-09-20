const express = require('express');
const bodyParser = require('body-parser');
const app = express();
const PORT = process.env.PORT || 5000;

app.use(bodyParser.json());

app.post('/analyze', (req, res) => {
    const text = req.body.text; // Asegúrate de enviar el texto en el cuerpo de la solicitud
    // Aquí integrarías la lógica para analizar el texto y devolver las emociones
    res.json({ emotion: "ejemplo de emoción" }); // Cambia esto por el análisis real
});

app.listen(PORT, () => {
    console.log(`Servidor escuchando en el puerto ${PORT}`);
});
