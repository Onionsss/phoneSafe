package heima.it.safe.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import heima.it.safe.R;
import heima.it.safe.bean.ContactInfo;

public class ContactUtil {
	
	//获取联系人的方法
	public static List<ContactInfo> getAllContacts(Context context){
		ContentResolver cr = context.getContentResolver();
		
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.NUMBER,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
        String selection = null;
		String[] selectionArgs = null;
		String sortOrder = null;
		Log.d("TAG", "getAllContacts: "+uri);
		List<ContactInfo> mData = new ArrayList<ContactInfo>();
		Cursor cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
		if (cursor != null) {
			while(cursor.moveToNext()){
				String name = cursor.getString(0);
				String number = cursor.getString(1);
				long contactId = cursor.getLong(2);
				
				ContactInfo info = new ContactInfo();
				info.setName(name);
				info.setAddress(number);
				Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_contact);
				info.setHead(getBitmap(context,contactId) == null?bitmap:getBitmap(context,contactId));
				mData.add(info);
			}
			cursor.close();
		}
		return mData;
	}
	
	//获取联系人头像
	public static Bitmap getBitmap(Context context, long contactId){
		ContentResolver cr = context.getContentResolver();
		Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactId));
//		uri:   content/......android/contact/id=具体的人?
		InputStream is = ContactsContract.Contacts.openContactPhotoInputStream(cr, contactUri);
		
		Bitmap bitmap = BitmapFactory.decodeStream(is);
		
		return bitmap;
	}

}
