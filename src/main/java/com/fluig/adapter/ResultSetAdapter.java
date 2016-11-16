package com.fluig.adapter;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ResultSetAdapter extends TypeAdapter<ResultSet> {
	private static final Gson gson = new Gson();

	public static class NotImplemented extends RuntimeException {
		private static final long serialVersionUID = -1103521735676997373L;
	}

	public ResultSet read(JsonReader reader) throws IOException {
		throw new NotImplemented();
	}

	public void write(JsonWriter writer, ResultSet rs) throws IOException {
		try {
			ResultSetMetaData meta = rs.getMetaData();
			int cc = meta.getColumnCount();

			writer.beginArray();
			rs.first();
			while (rs.next()) {
				writer.beginObject();
				for (int i = 1; i <= cc; ++i) {
					writer.name(meta.getColumnName(i));
					Class<?> type = Class.forName(meta.getColumnClassName(i));
					gson.toJson(rs.getObject(i), type, writer);
				}
				writer.endObject();
			}
			writer.endArray();
		} catch (SQLException e) {
			throw new RuntimeException(e.getClass().getName(), e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getClass().getName(), e);
		}
	}
}