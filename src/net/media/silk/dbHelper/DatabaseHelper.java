package net.media.silk.dbHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.media.silk.model.Accessno;
import net.media.silk.model.Conference;
import net.media.silk.model.Passcode;
import net.media.silk.model.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "SCM.db";

	// user table name
	private static final String TABLE_USER = "user";

	// conference name table name
	private static final String TABLE_CONF = "conference";

	// access number table name
	private static final String TABLE_ACCESSNO = "accessnumber";

	// passcode table name
	private static final String TABLE_PASSCODE = "passcode";

	// user Table Columns names
	private static final String U_ID = "uid";
	private static final String U_NAME = "name";
	private static final String U_PASSWORD = "password";
	private static final String U_CREATED_AT = "created";

	// conference Table Columns names
	private static final String C_ID = "cid";
	private static final String C_A_ID = "aid";
	private static final String C_P_ID = "pid";
	private static final String C_NAME = "confname";
	private static final String C_ACTIVE = "active";
	private static final String C_CREATED_AT = "created";
	private static final String C_UPDATED_AT = "updated";

	// access number Table Columns names
	private static final String A_ID = "aid";
	private static final String A_ACCESSNO = "accessno";
	private static final String A_ACTIVE = "active";
	private static final String A_CREATED_AT = "created";
	private static final String A_UPDATED_AT = "updated";

	// passcode Table Columns names
	private static final String P_ID = "pid";
	private static final String P_PASSCODE = "passcode";
	private static final String P_ACTIVE = "active";
	private static final String P_CREATED_AT = "created";
	private static final String P_UPDATED_AT = "updated";

	// user table create statement
	private static final String CREATE_TABLE_USER = "CREATE TABLE "
			+ TABLE_USER + "(" + U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ U_NAME + " TEXT," + U_PASSWORD + " TEXT," + U_CREATED_AT
			+ " DATETIME" + ")";

	// conference table create statement
	private static final String CREATE_TABLE_CONF = "CREATE TABLE "
			+ TABLE_CONF + "(" + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ C_A_ID + " INTEGER," + C_P_ID + " INTEGER," + C_NAME + " TEXT,"
			+ C_ACTIVE + " BOOLEAN," + C_CREATED_AT + " DATETIME,"
			+ C_UPDATED_AT + " DATETIME" + ")";

	// access number table create statement
	private static final String CREATE_TABLE_ACCESSNO = "CREATE TABLE "
			+ TABLE_ACCESSNO + "(" + A_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + A_ACCESSNO + " TEXT,"
			+ A_ACTIVE + " BOOLEAN," + A_CREATED_AT + " DATETIME,"
			+ A_UPDATED_AT + " DATETIME" + ")";

	// passcode table create statement
	private static final String CREATE_TABLE_PASSCODE = "CREATE TABLE "
			+ TABLE_PASSCODE + "(" + P_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + P_PASSCODE + " TEXT,"
			+ P_ACTIVE + " BOOLEAN," + P_CREATED_AT + " DATETIME,"
			+ P_UPDATED_AT + " DATETIME" + ")";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// creating required tables
		db.execSQL(CREATE_TABLE_USER);
		db.execSQL(CREATE_TABLE_CONF);
		db.execSQL(CREATE_TABLE_ACCESSNO);
		db.execSQL(CREATE_TABLE_PASSCODE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONF);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCESSNO);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSCODE);

		// create new tables
		onCreate(db);
	}

	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

	// get datetime
	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

	// insert user
	public long insertUser(User user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(U_NAME, user.getName());
		values.put(U_PASSWORD, user.getPassword());
		values.put(U_CREATED_AT, getDateTime());

		// insert row
		long user_id = db.insert(TABLE_USER, null, values);
		return user_id;
	}

	// insert conference
	public long insertConference(Conference conference) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(C_A_ID, conference.getAid());
		values.put(C_P_ID, conference.getPid());
		values.put(C_NAME, conference.getConfname().toUpperCase().trim());
		values.put(C_ACTIVE, conference.isActive());
		values.put(C_CREATED_AT, getDateTime());
		values.put(C_UPDATED_AT, getDateTime());

		// insert row
		long conf_id = db.insert(TABLE_CONF, null, values);
		return conf_id;
	}

	// insert access number
	public long insertAccessno(Accessno accessno) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(A_ACCESSNO, accessno.getAccessno().trim());
		values.put(A_ACTIVE, accessno.isActive());
		values.put(A_CREATED_AT, getDateTime());
		values.put(A_UPDATED_AT, getDateTime());

		// insert row
		long accessno_id = db.insert(TABLE_ACCESSNO, null, values);
		return accessno_id;
	}

	// insert passcode
	public long insertPasscode(Passcode passcode) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(P_PASSCODE, passcode.getPasscode().trim());
		values.put(P_ACTIVE, passcode.isActive());
		values.put(P_CREATED_AT, getDateTime());
		values.put(P_UPDATED_AT, getDateTime());

		// insert row
		long passcode_id = db.insert(TABLE_PASSCODE, null, values);
		return passcode_id;
	}

	// get all user
	public List<User> getUser() {
		List<User> usersList = new ArrayList<User>();
		String selectQuery = "SELECT  * FROM " + TABLE_USER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				User td = new User();
				td.setUid(c.getInt(c.getColumnIndex(U_ID)));
				td.setName(c.getString(c.getColumnIndex(U_NAME)));
				td.setPassword(c.getString(c.getColumnIndex(U_PASSWORD)));
				td.setCreated_at(c.getString(c.getColumnIndex(U_CREATED_AT)));

				// adding to pin list
				usersList.add(td);
			} while (c.moveToNext());
		}
		return usersList;
	}

	// get user
	public User getUser(String uname, String password) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE "
				+ U_NAME + " ='" + uname + "' AND " + U_PASSWORD + " ='"
				+ password + "'";

		Cursor c = db.rawQuery(selectQuery, null);
		User td = null;

		if (c != null && c.moveToFirst()) {
			td = new User();
			td.setUid(c.getInt(c.getColumnIndex(U_ID)));
			td.setName(c.getString(c.getColumnIndex(U_NAME)));
			td.setPassword(c.getString(c.getColumnIndex(U_PASSWORD)));
			td.setCreated_at(c.getString(c.getColumnIndex(U_CREATED_AT)));
		}
		return td;
	}

	// get all conference
	public List<Conference> getConference() {
		List<Conference> conferencesList = new ArrayList<Conference>();
		String selectQuery = "SELECT  * FROM " + TABLE_CONF + " WHERE "
				+ P_ACTIVE + " =1";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Conference td = new Conference();
				td.setCid(c.getInt((c.getColumnIndex(C_ID))));
				td.setAid(c.getInt((c.getColumnIndex(C_A_ID))));
				td.setPid(c.getInt((c.getColumnIndex(C_P_ID))));
				td.setConfname((c.getString(c.getColumnIndex(C_NAME))));
				int flag = c.getInt(c.getColumnIndex(P_ACTIVE));
				if (flag == 0) {
					td.setActive(false);
				} else {
					td.setActive(true);
				}
				td.setCreated(c.getString(c.getColumnIndex(C_CREATED_AT)));
				td.setUpdated(c.getString(c.getColumnIndex(C_CREATED_AT)));

				// adding to pin list
				conferencesList.add(td);
			} while (c.moveToNext());
		}
		return conferencesList;
	}

	// get all access number
	public List<Accessno> getAccessno() {
		List<Accessno> accessnoList = new ArrayList<Accessno>();
		String selectQuery = "SELECT  * FROM " + TABLE_ACCESSNO;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Accessno td = new Accessno();
				td.setAid(c.getInt(c.getColumnIndex(A_ID)));
				td.setAccessno(c.getString(c.getColumnIndex(A_ACCESSNO)));
				int flag = c.getInt(c.getColumnIndex(A_ACTIVE));
				if (flag == 0) {
					td.setActive(false);
				} else {
					td.setActive(true);
				}
				td.setCreated(c.getString(c.getColumnIndex(A_CREATED_AT)));
				td.setUpdated(c.getString(c.getColumnIndex(A_UPDATED_AT)));

				// adding to pin list
				accessnoList.add(td);
			} while (c.moveToNext());
		}
		return accessnoList;
	}

	// get access number
	public Accessno getAccessno(int aid) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_ACCESSNO + " WHERE "
				+ A_ID + " ='" + aid + "'";

		Cursor c = db.rawQuery(selectQuery, null);
		Accessno td = null;

		if (c != null && c.moveToFirst()) {
			td = new Accessno();
			td.setAid(c.getInt(c.getColumnIndex(A_ID)));
			td.setAccessno(c.getString(c.getColumnIndex(A_ACCESSNO)));
			int flag = c.getInt(c.getColumnIndex(A_ACTIVE));
			if (flag == 0) {
				td.setActive(false);
			} else {
				td.setActive(true);
			}
			td.setCreated(c.getString(c.getColumnIndex(A_CREATED_AT)));
			td.setUpdated(c.getString(c.getColumnIndex(A_UPDATED_AT)));
		}
		return td;
	}

	// get access number
	public Accessno getAccessno(String accessno) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_ACCESSNO + " WHERE "
				+ A_ACCESSNO + " ='" + accessno + "'";

		Cursor c = db.rawQuery(selectQuery, null);
		Accessno td = null;

		if (c != null && c.moveToFirst()) {
			td = new Accessno();
			td.setAid(c.getInt(c.getColumnIndex(A_ID)));
			td.setAccessno(c.getString(c.getColumnIndex(A_ACCESSNO)));
			int flag = c.getInt(c.getColumnIndex(A_ACTIVE));
			if (flag == 0) {
				td.setActive(false);
			} else {
				td.setActive(true);
			}
			td.setCreated(c.getString(c.getColumnIndex(A_CREATED_AT)));
			td.setUpdated(c.getString(c.getColumnIndex(A_UPDATED_AT)));
		}
		return td;
	}

	// get all passcode
	public List<Passcode> getPasscode() {
		List<Passcode> passcodeList = new ArrayList<Passcode>();
		String selectQuery = "SELECT  * FROM " + TABLE_PASSCODE;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Passcode td = new Passcode();
				td.setPid(c.getInt(c.getColumnIndex(P_ID)));
				td.setPasscode(c.getString(c.getColumnIndex(P_PASSCODE)));
				int flag = c.getInt(c.getColumnIndex(P_ACTIVE));
				if (flag == 0) {
					td.setActive(false);
				} else {
					td.setActive(true);
				}
				td.setCreated(c.getString(c.getColumnIndex(P_CREATED_AT)));
				td.setUpdated(c.getString(c.getColumnIndex(P_UPDATED_AT)));

				// adding to pin list
				passcodeList.add(td);
			} while (c.moveToNext());
		}
		return passcodeList;
	}

	// get passcode
	public Passcode getPasscode(int pid) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_PASSCODE + " WHERE "
				+ P_ID + " ='" + pid + "'";

		Cursor c = db.rawQuery(selectQuery, null);
		Passcode td = null;

		if (c != null && c.moveToFirst()) {
			td = new Passcode();
			td.setPid(c.getInt(c.getColumnIndex(P_ID)));
			td.setPasscode(c.getString(c.getColumnIndex(P_PASSCODE)));
			int flag = c.getInt(c.getColumnIndex(P_ACTIVE));
			if (flag == 0) {
				td.setActive(false);
			} else {
				td.setActive(true);
			}
			td.setCreated(c.getString(c.getColumnIndex(P_CREATED_AT)));
			td.setUpdated(c.getString(c.getColumnIndex(P_UPDATED_AT)));
		}
		return td;
	}

	// get passcode
	public Passcode getPasscode(String passcode) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_PASSCODE + " WHERE "
				+ P_PASSCODE + " ='" + passcode + "'";

		Cursor c = db.rawQuery(selectQuery, null);
		Passcode td = null;

		if (c != null && c.moveToFirst()) {
			td = new Passcode();
			td.setPid(c.getInt(c.getColumnIndex(P_ID)));
			td.setPasscode(c.getString(c.getColumnIndex(P_PASSCODE)));
			int flag = c.getInt(c.getColumnIndex(P_ACTIVE));
			if (flag == 0) {
				td.setActive(false);
			} else {
				td.setActive(true);
			}
			td.setCreated(c.getString(c.getColumnIndex(P_CREATED_AT)));
			td.setUpdated(c.getString(c.getColumnIndex(P_UPDATED_AT)));
		}
		return td;
	}

	// get conference is available or not
	public boolean isConference(String confname) {
		String selectQuery = "SELECT  * FROM " + TABLE_CONF + " WHERE "
				+ C_NAME + " ='" + confname.toUpperCase().trim() + "'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			return true;
		} else {
			return false;
		}
	}

	// get access number is available or not
	public boolean isAccessno(String accessno) {
		String selectQuery = "SELECT  * FROM " + TABLE_ACCESSNO + " WHERE "
				+ A_ACCESSNO + " ='" + accessno + "'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			return true;
		} else {
			return false;
		}
	}

	// get passcode is available or not
	public boolean isPasscode(String passcode) {
		String selectQuery = "SELECT  * FROM " + TABLE_PASSCODE + " WHERE "
				+ P_PASSCODE + " ='" + passcode + "'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			return true;
		} else {
			return false;
		}
	}

	// get passcode count (total number of passcode)
	public int getPasscodeCount() {
		String selectQuery = "SELECT  * FROM " + TABLE_PASSCODE;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			return c.getCount();
		} else {
			return 0;
		}
	}

	// change password
	public int changePassword(User user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(U_PASSWORD, user.getPassword());

		// updating row
		return db.update(TABLE_USER, values, U_ID + " = ?",
				new String[] { String.valueOf(user.getUid()) });
	}

	// Updating conference
	public int updateConference(Conference conference) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(C_NAME, conference.getConfname().toUpperCase().trim());
		values.put(C_ACTIVE, conference.isActive());
		values.put(C_UPDATED_AT, getDateTime());

		// updating row
		return db.update(TABLE_CONF, values, C_ID + " = ?",
				new String[] { String.valueOf(conference.getCid()) });
	}

	// Updating conference
	public int updateConference(int pid) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();		
		values.put(C_ACTIVE, true);
		values.put(C_UPDATED_AT, getDateTime());

		// updating row
		return db.update(TABLE_CONF, values, C_P_ID + " = ?",
				new String[] { String.valueOf(pid) });
	}

	// Updating access no
	public int updateAccessno(Accessno accessno) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(A_ACCESSNO, accessno.getAccessno().trim());
		values.put(A_ACTIVE, accessno.isActive());
		values.put(A_UPDATED_AT, getDateTime());

		// updating row
		return db.update(TABLE_ACCESSNO, values, A_ID + " = ?",
				new String[] { String.valueOf(accessno.getAid()) });
	}

	// Updating passcode
	public int updatePasscode(Passcode passcode) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(P_PASSCODE, passcode.getPasscode().trim());
		values.put(P_ACTIVE, passcode.isActive());
		values.put(P_UPDATED_AT, getDateTime());

		// updating row
		return db.update(TABLE_PASSCODE, values, P_ID + " = ?",
				new String[] { String.valueOf(passcode.getPid()) });
	}

	// delete conference
	public int deleteConference(int cid) {
		SQLiteDatabase db = this.getWritableDatabase();

		// updating row
		return db.delete(TABLE_CONF, C_ID + " = ?",
				new String[] { String.valueOf(cid) });
	}

	// delete access number
	public int deleteAccessno(int aid) {
		SQLiteDatabase db = this.getWritableDatabase();

		// updating row
		return db.delete(TABLE_ACCESSNO, A_ID + " = ?",
				new String[] { String.valueOf(aid) });
	}

	// delete passcode
	public int deletePasscode(int pid) {
		SQLiteDatabase db = this.getWritableDatabase();

		// updating row
		return db.delete(TABLE_PASSCODE, P_ID + " = ?",
				new String[] { String.valueOf(pid) });
	}
}
