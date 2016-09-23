package tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StepTool {
	//计算消耗热量
	public static double calc_calories(double weight,int fastStepNumber,int slowStepNumer){
		double[] timearr=cal_step_time(fastStepNumber,slowStepNumer);
		return  timearr[0]*weight*4.4+timearr[1]*3.1*weight;
	}
	
	//计算步行时间
	public static double[] cal_step_time(int fastStepNumber,int slowStepNumber){
		return new double[]{((double)fastStepNumber*0.5/3600),(double) (slowStepNumber*0.75/3600)};
	}
	
	public static double cal_total_time(int fastStepNumber,int slowStepNumber)
	{
		double[] times= cal_step_time(fastStepNumber,slowStepNumber);
		return (times[0]+times[1])*60;
	}
	
	//判断是否是快走
	public static boolean isFastStep(int stepnumber,int min){
		return stepnumber/min>120;
	}
	
	//根据步幅计算步行距离
	public static int stepDistance(int slowStride,int slowStepNumber,int fastStride,int fastNumber){
		return (slowStride * slowStepNumber + fastStride * fastNumber) / 100;
	}
	
	//根据身高计算步行距离
	public static int calStepDistance(int height,int slowStepNumber,int fastStepNumber){
		return stepDistance(cal_strip(height,false), slowStepNumber, cal_strip(height,true), fastStepNumber);
	}
	
	//计算步幅
	public static int cal_strip(int height,boolean isfast){
		int cval= isfast?110:100;
		return height-cval;
	}
	//计算BMI
	private static int cal_bmi(int height,int weight,String gender){
		int index=get_gender_index(gender);
		int bmi=(int)(weight/(((float)height/100)*((float)height/100)));
		return index<0?0:calBmiVal(index,bmi);
	}
	
	private static int calBmiVal(int index,int bmi){
		int[][][] range=new int[][][]{{{0,19},{19,24},{24,29},{29,34},{34,51}},{{0,20},{20,25},{25,30},{30,35},{35,51}}};
		int[][] vals= range[index];
		for(int i=0;i<vals.length;i++){
			if(bmi>=vals[i][0]&&bmi<vals[i][1]){
				return i;
			}
		}
		return 0;
	}
	
	//计算速度
	public static double calStepSpeed(int steps,int height){
		return steps*cal_strip(height,false)/100000.0;
	}
	
	private static int get_gender_index(String gender){
		String[] sex={"F","M"};
		for(int i=0;i<sex.length;i++){
			if(sex[i]==gender){
				return i;
			}
		}
		return -1;
	}
	
	public static int[] cal_step_goal(int age,String[] dis_code,int height,int weight,String gender){
		int bmi=cal_bmi(height,weight,gender);
		String caldis=bmi > 2?"FPZ":bmi<1?"XSZ":"";
		List<String> mylist=new ArrayList<String>();
		Collections.addAll(mylist, dis_code);
		if(caldis!=""){mylist.add(caldis);}
		if(mylist.size()==0){return calGoal("NORMAL",age);}
		List<int[]> goals=new ArrayList<int[]>();
		for(String item:mylist){
			int[] goal= calGoal(item,age);
			if(goal!=null){goals.add(goal);}
		}
		return calGoal(goals);
	}
	
	static int[] calGoal(List<int[]> goals){
		if(goals.size()==0){return new int[]{0,0};}
		int[] goal={goals.get(0)[0],goals.get(0)[1]};
		for(int[] item:goals){
			goal[0]=item[0]<goal[0]?item[0]:goal[0];
			goal[1]=item[1]<goal[1]?item[1]:goal[1];
		}
		return goal;
	}
	
	static int[] calGoal(String discode,int age){
		String[][] sourceRules = new String[][] { 
                //new String[]{疾病,最小年龄,最大年龄,总步数,快步步数}
                new String[]{"GXB","0","200","5000","0"},
                new String[]{"TNB","0","200","6000","0"},
                new String[]{"FPZ","0","200","8000","0"},
                new String[]{"NORMAL","0","55","10000","8000"},
                new String[]{"NORMAL","56","60","9000","6300"},
                new String[]{"NORMAL","61","65","8000","4000"},
                new String[]{"NORMAL","66","70","7000","2800"},
                new String[]{"NORMAL","71","75","6000","1800"},
                new String[]{"NORMAL","76","200","5000","0"},
                new String[]{"XSZ","0","55","5000","4000"},
                new String[]{"XSZ","56","60","5000","3500"},
                new String[]{"XSZ","61","65","5000","2500"},
                new String[]{"XSZ","66","70","5000","2000"},
                new String[]{"XSZ","71","75","5000","1500"},
                new String[]{"XSZ","76","200","5000","0"}
        };
		for(String[] item:sourceRules){
			if(item[0]==discode&&age>=Integer.parseInt(item[1])&&age<Integer.parseInt(item[2])){
				 return new int[]{Integer.parseInt(item[3]),Integer.parseInt(item[4])};
			}
		}
		return null;
	}
}
