db = db.getSiblingDB("playtomic");

// create collections
db.createCollection("wallets");
db.wallets.createIndex({ userId: 1 }, { unique: true }); // Index on userId

db.createCollection("transactions");
db.transactions.createIndex({ walletId: 1, createdAt: -1 }); // Compound index

// add sample wallet
db.wallets.insertOne({
    _id: "wallet1", // dummy id
    userId: "U1",
    balance: {
        value: 10000, // 100 EUR
        decimal: 2,
        currency: "EUR"
    },
    createdAt: new Date(),
    updatedAt: new Date()
});
