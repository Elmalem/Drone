package Auto_pack;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

/*
 * Unchanged
 */

public class CPU {
	private int hz; 
	private List<IntConsumer> functions_list;
	private boolean isPlay;
	private long elapsedMilli;
	private boolean isPlayedBeforeStop;
	private Thread thread;
	
	public CPU(int hz,String name) {
		functions_list = new ArrayList<>();
		isPlay = false;
		isPlayedBeforeStop = false;
		
		this.hz = hz;
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

		if(GameVariabales.all_cpus == null) {
			GameVariabales.all_cpus = new ArrayList<>();
		}
		
		GameVariabales.all_cpus.add(this);
	}
	
	synchronized void resume() {
		if(isPlayedBeforeStop) {
			isPlay = true;
			notify();
		}
   }

	
	public void addFunction(IntConsumer a) {
		functions_list.add(a);
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
	
	public void setPlay() {
		this.isPlay = !this.isPlay;
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
		if(1000/hz > 1) {
			time_to_sleep = 1000/hz;
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
			} catch (InterruptedException e) {}	
			
		    int diff = (int)(System.currentTimeMillis()-last_sample);
		    int before_index = getCyclic(i-1,functions_size);
		    int actual_diff = last_sample_times[before_index] + diff - last_sample_times[i];
		    last_sample_times[i] = last_sample_times[before_index] + diff;
		    
		    IntConsumer curr_func = functions_list.get(i);
		    curr_func.accept(actual_diff);
		    elapsedMilli += actual_diff;
		    i++;
		    i %= functions_size;
		}
	}
	
	private int getCyclic(int i,int size) {
		i %= size;
		if(i< 0) {
			return size+i;
		}
		return i;
	}


}