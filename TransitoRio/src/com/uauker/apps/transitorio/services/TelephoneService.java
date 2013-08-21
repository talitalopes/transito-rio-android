package com.uauker.apps.transitorio.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.uauker.apps.transitorio.R;
import com.uauker.apps.transitorio.models.others.Telephone;

public class TelephoneService {

	private List<Telephone> telephones = new ArrayList<Telephone>();

	private Context context;

	public TelephoneService(Context context) {
		this.context = context;
	}

	public List<Telephone> getTelephones() {
		loadTelephones();
		return telephones;
	}

	public void loadTelephones() {
		try {
			InputStream rawResource = context.getResources().openRawResource(
					R.raw.telephone);

			String json = readFully(rawResource);
			JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();

			Gson gson = new Gson();

			Iterator<JsonElement> it = jsonArray.iterator();

			while (it.hasNext()) {
				JsonElement placeJson = it.next();
				Telephone telephone = gson.fromJson(placeJson, Telephone.class);
				this.telephones.add(telephone);
			}			
		} catch (IOException e) {
			Log.e("Erro ao carregar o arquivo de place", e.getMessage());
		}
	}

	private String readFully(InputStream inputStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = inputStream.read(buffer)) != -1) {
			baos.write(buffer, 0, length);
		}
		return new String(baos.toByteArray());
	}
}
