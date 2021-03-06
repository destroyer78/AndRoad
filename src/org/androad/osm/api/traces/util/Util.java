// Created by plusminus on 13:24:05 - 21.09.2008
package org.androad.osm.api.traces.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.osmdroid.contributor.util.RecordedGeoPoint;
import org.osmdroid.util.BoundingBoxE6;


/**
 * 
 * @author Nicolas Gramlich
 *
 */
public class Util implements OSMTraceAPIConstants{
	// ===========================================================
	// Constants
	// ===========================================================

	public static final SimpleDateFormat UTCSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	{
		UTCSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static final String convertTimestampToUTCString(final long aTimestamp){
		return UTCSimpleDateFormat.format(new Date(aTimestamp));
	}

	public static boolean isSufficienDataForUpload(final ArrayList<RecordedGeoPoint> recordedGeoPoints){
		if(recordedGeoPoints == null) {
			return false;
		}

		if(recordedGeoPoints.size() < MINGEOPOINTS_FOR_OSM_CONTRIBUTION) {
			return false;
		}

		final BoundingBoxE6 bb = BoundingBoxE6.fromGeoPoints(recordedGeoPoints);
		final int diagMeters = bb.getDiagonalLengthInMeters();
		if(diagMeters < MINDIAGONALMETERS_FOR_OSM_CONTRIBUTION) {
			return false;
		}

		return true;
	}


	/**
	 * @param data the data to be zipped.
	 * @param aZipEntryFileName sth like <code>"myfile.txt.zip"</code>
	 * @param aOutputStream to write the zipped data to
	 * @return the Zip-Compressed bytes, saveable as a valid <code>*.zip</code>-File.
	 * @throws IOException
	 */
	public static void zipBytesToOutputStream(final byte[] data, final String aZipEntryFileName, final OutputStream aOutputStream) throws IOException {
		//		Assert.assertNotNull(aOutputStream);
		aOutputStream.write(zipBytes(data, aZipEntryFileName));
		aOutputStream.flush();
		aOutputStream.close();
	}

	/**
	 * @param data the data to be zipped.
	 * @param aZipEntryFileName sth like <code>"myfile.txt.zip"</code>
	 * @return the Zip-Compressed bytes, saveable as a valid <code>*.zip</code>-File.
	 * @throws IOException
	 */
	public static byte[] zipBytes(final byte[] data, final String aZipEntryFileName) throws IOException {
		//		Assert.assertNotNull(data);
		//		Assert.assertNotNull(aFileName);
		//		Assert.assertTrue(aFileName.length() > 0);

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final ZipOutputStream zos = new ZipOutputStream(baos);
		zos.setLevel(9);
		zos.setComment("Timestamp: " + System.currentTimeMillis());

		zos.putNextEntry(new ZipEntry(aZipEntryFileName));

		zos.write(data);

		zos.flush();
		zos.close();

		return baos.toByteArray();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
