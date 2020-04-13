package Auto_pack;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

public class CPU {
	
	Thread thread;

	private long elapsedMilli; // Time signer

	private boolean isPlay; // Alive boolean
	private boolean isPlayedBeforeStop;
	
	public List<IntConsumer> functions_list;
	public static List<CPU> all_cpus = null;
		
	public CPU(String name) {
		functions_list = new ArrayList<>();
		isPlay = false;
		isPlayedBeforeStop = false;
		
		elapsedMilli = 0;
		thread = new Thread("Eventor_" + name){
	        public void run(){
	        	try {
					Thread.sleep(10);
				} catch (InterruptedException e) {}
	        	thread_run();
	        }
	      };
        thread.start();

		if(all_cpus == null) {
			all_cpus = new ArrayList<>();
		}
		
		all_cpus.add(this);
	}
	
	// move it to utils - cpus functions
	
	public static void stopAllCPUS() {
		for(int i=0;i<all_cpus.size();i++) {
			all_cpus.get(i).isPlay = false;
		}
	}
	
	public static void resumeAllCPUS() {
		for(int i=0; i<all_cpus.size(); i++) {
			all_cpus.get(i).resume();
		}
	}
	
	synchronized void resume() {
		if(isPlayedBeforeStop) {
			isPlay = true;
			notify();
		}
   }
	
	public void play() {
		isPlay = true;
		isPlayedBeforeStop = true;
		resume();
	}
	
	public void stop() {
		isPlay = false;
		isPlayedBeforeStop = false;
	}
	
	public long getElapsedMilli() {
		return this.elapsedMilli;
	}
	
	public void resetClock() {
		this.elapsedMilli = 0;
	}
	
	public void thread_run() {
		int functions_size= 0;
		int[] last_sample_times = null;
		long last_sample;
		int i=0;
		
		int time_to_sleep = 2;
		switch(i) {
		 case 0:
			 // Repaint
			 time_to_sleep = 1000 /  200;
			 break;
		 case 1:
			 // Drone update
			 time_to_sleep = 1000 /  60;
			 break;
		 case 2:
			 //  Info update
			 time_to_sleep = 1000 /  6;
			 break;
		}

		while(true) {
			
			if(functions_size != functions_list.size()) {
				functions_size = functions_list.size();
				last_sample_times = new int[functions_size];
				i=0;
			}
			if(functions_size == 0) {
				continue;
			}
			
			last_sample = System.currentTimeMillis();
			
			try {
			    Thread.sleep(time_to_sleep);
			    synchronized(this) {
		               while(!this.isPlay) {
		                  wait();
		                  last_sample = System.currentTimeMillis();
		               }
		            }
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
			
		    int diff = (int)(System.currentTimeMillis() - last_sample);
		    int before_index = (i % functions_size) < 0 ? (functions_size + i) : i;
		    int actual_diff = last_sample_times[before_index] + diff - last_sample_times[i];
		    last_sample_times[i] = last_sample_times[before_index] + diff;
		    
		    IntConsumer curr_func = functions_list.get(i);
		    curr_func.accept(actual_diff);
		    elapsedMilli += actual_diff;
		    i = (i+1) % functions_size;
		}
	}

}
