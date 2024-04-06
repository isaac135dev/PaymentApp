import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.credibanco.ui.theme.Data.Model.ResponseData
import com.example.credibanco.ui.theme.Data.Model.Transaction

class TransactionDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "transaction_database"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "transactions"
        private const val COLUMN_RECEIPT_ID = "receipt_id"
        private const val COLUMN_RRN = "rrn"
        private const val COLUMN_STATUS_CODE = "status_code"
        private const val COLUMN_STATUS_DESCRIPTION = "status_description"
    }

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> = _transactions

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_RECEIPT_ID TEXT PRIMARY KEY,
                $COLUMN_RRN TEXT,
                $COLUMN_STATUS_CODE TEXT,
                $COLUMN_STATUS_DESCRIPTION TEXT
            )
        """.trimIndent()

        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades if needed
    }

    fun insertTransaction(transaction: ResponseData) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_RECEIPT_ID, transaction.receiptId)
            put(COLUMN_RRN, transaction.rrn)
            put(COLUMN_STATUS_CODE, transaction.statusCode)
            put(COLUMN_STATUS_DESCRIPTION, transaction.statusDescription)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
        refreshTransactions()
    }

    fun getAllTransactionsLiveData(): LiveData<List<Transaction>> {
        refreshTransactions()
        return transactions
    }

    private fun refreshTransactions() {
        val db = readableDatabase
        val transactionsList = mutableListOf<Transaction>()
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val receiptId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECEIPT_ID))
                val rrn = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RRN))
                val statusCode = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS_CODE))
                val statusDescription = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS_DESCRIPTION))
                val transaction = Transaction(receiptId, rrn, statusCode, statusDescription)
                transactionsList.add(transaction)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        _transactions.postValue(transactionsList)
    }

    fun getTransactionByReceiptId(receiptId: String): Transaction? {
        val db = readableDatabase
        val selection = "$COLUMN_RECEIPT_ID = ?"
        val selectionArgs = arrayOf(receiptId)
        val cursor = db.query(
            TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var transaction: Transaction? = null

        if (cursor.moveToFirst()) {
            val rrn = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RRN))
            val statusCode = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS_CODE))
            val statusDescription = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS_DESCRIPTION))

            transaction = Transaction(receiptId, rrn, statusCode, statusDescription)
        }

        cursor.close()
        db.close()

        return transaction
    }

    fun updateTransaction(transaction: ResponseData): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_RECEIPT_ID, transaction.receiptId)
            put(COLUMN_RRN, transaction.rrn)
            put(COLUMN_STATUS_CODE, transaction.statusCode)
            put(COLUMN_STATUS_DESCRIPTION, transaction.statusDescription)
        }

        val whereClause = "$COLUMN_RECEIPT_ID = ?"
        val whereArgs = arrayOf(transaction.receiptId)

        val rowsAffected = db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()

        return rowsAffected
    }

}
