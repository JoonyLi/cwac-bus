/***
	Copyright (c) 2008-2009 CommonsWare, LLC
	
	Licensed under the Apache License, Version 2.0 (the "License"); you may
	not use this file except in compliance with the License. You may obtain
	a copy of the License at
		http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/

package com.commonsware.cwac.bus.demo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import java.util.concurrent.atomic.AtomicInteger;
import com.commonsware.cwac.bus.SimpleBus;
import com.commonsware.cwac.task.AsyncTaskEx;

public class BusDemoService extends Service {
	private static String[] items={"lorem", "ipsum", "dolor",
																	"sit", "amet", "consectetuer",
																	"adipiscing", "elit", "morbi",
																	"vel", "ligula", "vitae",
																	"arcu", "aliquet", "mollis",
																	"etiam", "vel", "erat",
																	"placerat", "ante",
																	"porttitor", "sodales",
																	"pellentesque", "augue",
																	"purus"};
	public static final String WORD=
						"com.commonsware.cwac.bus.demo.WORD";
	public static SimpleBus bus=new SimpleBus();
	public static BusDemoService singleton=null;
	private AtomicInteger refCount=new AtomicInteger(0);
	private StopTask stopTask=null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		singleton=this;
		
		new WordsTask().execute();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		refCount.incrementAndGet();
		
		if (stopTask!=null) {
			stopTask.cancel(true);
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return(null);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		singleton=null;
	}
	
	void thinkAboutStopping() {
		if (refCount.decrementAndGet()==0) {
			if (stopTask!=null) {
				stopTask.cancel(true);
			}
			
			stopTask=new StopTask();
			stopTask.execute();
		}
	}
	
	class WordsTask extends AsyncTaskEx<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... unused) {
			for (String word : items) {
				Bundle message=bus.createMessage("SampleKey");
				
				message.putString(WORD, word);
				bus.send(message);
				SystemClock.sleep(300);		// simulate real work
			}
			
			return(null);
		}
	}
	
	class StopTask extends AsyncTaskEx<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... unused) {
			SystemClock.sleep(30000);			// stop after 30 seconds
			
			return(null);
		}
		
		@Override
		protected void onPostExecute(Void unused) {
			if (refCount.get()==0) {
				stopSelf();
			}
		}
	}
}