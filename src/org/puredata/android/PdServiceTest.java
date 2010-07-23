/**
 * 
 * @author Peter Brinkmann (peter.brinkmann@gmail.com)
 * 
 * For information on usage and redistribution, and for a DISCLAIMER OF ALL
 * WARRANTIES, see the file, "LICENSE.txt," in this distribution.
 *
 * simple test case for {@link PdService}
 * 
 */

package org.puredata.android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.puredata.android.service.IPdClient;
import org.puredata.android.service.IPdListener;
import org.puredata.android.service.IPdService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class PdServiceTest extends Activity {

	private static final String PD_TEST = "Pd Test";
	private IPdService proxy = null;
	
	private String folder, filename, patch;
	private int sampleRate, nIn, nOut, ticksPerBuffer;
	
	private final IPdClient.Stub client = new IPdClient.Stub() {
		
		@Override
		public void handleStop() throws RemoteException {
			Log.i(PD_TEST, "audio thread stopped");
			finish();
		}
		
		@Override
		public void handleStart(int sampleRate, int nIn, int nOut, int ticksPerBuffer) throws RemoteException {
			Log.i(PD_TEST, "audio thread started: " + sampleRate + ", " + nIn + ", " + nOut + ", " + ticksPerBuffer);
		}
	};

	private static class Receiver extends IPdListener.Stub {

		private final String tag;
		
		private Receiver(String source) {
			tag = PD_TEST + " " + source;
		}
		
		@Override
		public void receiveBang() throws RemoteException {
			Log.i(tag, "bang!");
		}

		@Override
		public void receiveFloat(float x) throws RemoteException {
			Log.i(tag, "float: " + x);
		}
		
		@Override
		public void receiveSymbol(String symbol) throws RemoteException {
			Log.i(tag, "symbol: " + symbol);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void receiveList(List args) throws RemoteException {
			Log.i(tag, "args: " + args.toString());
		}

		@SuppressWarnings("unchecked")
		@Override
		public void receiveMessage(String symbol, List args)
				throws RemoteException {
			Log.i(tag, "symbol: " + symbol + ", args: " + args.toString());
		}
	};
	
	private Receiver spam = new Receiver("Spam");
	private Receiver eggs = new Receiver("Eggs");
	
	private final ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			proxy = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			proxy = IPdService.Stub.asInterface(service);
			try {
				proxy.addClient(client);
				proxy.subscribe("spam", spam);
				proxy.subscribe("eggs", eggs);
				proxy.sendMessage("pd", "open", Arrays.asList(new Object[] {filename, folder}));
				proxy.requestAudio(sampleRate, nIn, nOut, ticksPerBuffer);
				proxy.sendBang("foo");
				proxy.sendFloat("foo", 12345);
				proxy.sendSymbol("bar", "elephant");
				proxy.sendList("bar", Arrays.asList(new Object[] { new Integer(5), "katze", new Float(1.414) }));
				proxy.sendMessage("bar", "boing", Arrays.asList(new Object[] { new Integer(5), "katze", new Float(1.414) }));
			} catch (RemoteException e) {
				Log.e(PD_TEST, e.toString());
			}
		}
	};
	
	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Resources res = getResources();
		folder = res.getString(R.string.folder);
		filename = res.getString(R.string.patch);
		patch = "pd-" + filename;
		sampleRate = res.getInteger(R.integer.sampleRate);
		nIn = res.getInteger(R.integer.inChannels);
		nOut = res.getInteger(R.integer.outChannels);
		ticksPerBuffer = res.getInteger(R.integer.ticksPerBuffer);
	};
	
	@Override
	protected void onStart() {
		super.onStart();
		bindService(new Intent("org.puredata.android.service.LAUNCH"), connection, BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {
		super.onStop();
		cleanup();
		unbindService(connection);
	}

	@SuppressWarnings("unchecked")
	private void cleanup() {
		if (proxy != null) {
			try {
				proxy.removeClient(client);
				proxy.sendMessage(patch, "menuclose", new ArrayList());
				proxy.unsubscribe("spam", spam);
				proxy.unsubscribe("eggs", eggs);
				proxy.releaseAudio();
			} catch (RemoteException e) {
				Log.e(PD_TEST, e.toString());
			}
		}
	}
}
