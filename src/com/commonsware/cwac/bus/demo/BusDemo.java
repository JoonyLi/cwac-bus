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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import com.commonsware.cwac.bus.SimpleBus;

public class BusDemo extends ListActivity {
	ArrayAdapter<String> adapter=null;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		
		adapter=(ArrayAdapter<String>)getLastNonConfigurationInstance();
		
		if (adapter==null) {
			adapter=new ArrayAdapter<String>(this,
																				android.R.layout.simple_list_item_1,
																				new ArrayList<String>());
		}
		
		setListAdapter(adapter);
		
		BusDemoService.bus.register("SampleKey", wordReceiver,
																"demo");
		
		startService(new Intent(this, BusDemoService.class));
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if (BusDemoService.singleton!=null) {
			BusDemoService.singleton.thinkAboutStopping();
		}
		
		BusDemoService.bus.unregister(wordReceiver,
																		new LinkedBlockingQueue<SoftReference<Bundle>>());
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		return(adapter);
	}
	
	private SimpleBus.Receiver wordReceiver=new SimpleBus.Receiver<Bundle>() {
		public void onReceive(Bundle message) {
			final String word=message.getString(BusDemoService.WORD);
			
			runOnUiThread(new Runnable() {
				public void run() {
					adapter.add(word);
				}
			});
		}
	};
}
