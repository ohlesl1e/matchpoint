const functions = require("firebase-functions");
const admin = require('firebase-admin');
const { database } = require("firebase-admin");
admin.initializeApp(functions.config().firebase)

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
exports.helloWorld = functions.https.onRequest((request, response) => {
    functions.logger.info("Hello logs!", { structuredData: true });
    response.send("Hello from Firebase!");
});

exports.signup = functions.https.onRequest(async (request, response) => {
    var credential = {
        email: request.body.email,
        password: request.body.password,
    }
    const db = admin.firestore().collection('users')
    const snapshot = await db.where('email', '==', credential.email).get()

    if (!snapshot.empty) {
        console.log('email existed');
        return response.send('email existed')
    } else {
        await db.doc().set(credential)
        return response.send('success')
    }
})