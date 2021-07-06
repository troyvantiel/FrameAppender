import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;
public class FileProcess
{
	public static double volMean = 0;
	public static double numpointsMean = 0;
	public static double kinEnergyMean = 0;
	public static double potEnergyMean = 0;
	public static double totEnergyMean = 0;
	public static double elfMean = 0;
	public static double rhoMean = 0;





	public static void AddMean(List<String> numpoints,List<String> vol,List<String> kinEnergy,List<String> potEnergy,List<String> totEnergy,List<String> elf,List<String> rho)
	{
		for(int k = 0; k < args[1]; k++)
		{

		}

	}

	public static List<String> CalcMeanSTD(List<String> numpoints, vol, kinEnergy, potEnergy, totEnergy, elf, rho, int div)
	{
		for(int k = 0; k < args[1]; k++)
		{
			volMean += vol[k];
			numpointsMean += numpoints[k];
			kinEnergyMean += kinEnergy[k];
			potEnergyMean += potEnergy[k];
			totEnergyMean += totEnergy[k];
			elfMean += elf[k];
			rhoMean += rho[k];


		}
		List<String> Mean = new ArrayList();
		Mean.add(numpointsMean/div);
		Mean.add(volMean/div);
		Mean.add(kinEnergyMean/div);
		Mean.add(potEnergyMean/div);
		Mean.add(totEnergyMean/div);
		Mean.add(elfMean/div);
		Mean.add(rhoMean/div);
		
		for(int i = 0; i < Mean.length(); i++)
		{
			for(string num: )
			{
				int stdDev += Math.pow(Integer.parseInt(num) - )
			}
		}
	}










    //args[0] is the filename without the number in it (String)
    //args[1] is the number of files that need to be processed (int)
    //args[2]line number that holds the value wanted (int)
    //args[3]output file name (String)
    //args[4] determines which file output is being used can be (m,t,p) which is minus, positive or total interactions.
    public static void main(String[] args) 
    {
        if(args.length != 5)
        {
            System.out.println("Usage:");
            System.out.println("Argument 1 = filename without the frame number in it");
            System.out.println("Argument 2 = Number of files that need to be processed (number of frames)");
            System.out.println("Argument 3 = Line number within the .csv files being processed that holds the value wanted");
            System.out.println("Argument 4 = Name of the output file");
            System.out.println("Argument 5 = the type of file being read (options are)");
            System.out.println("                m - minus interaction (attractive)");
            System.out.println("                p - positive interaction (repulsive)");
            System.out.println("                t - total interaction (sum of previous files)");
        }
        else
        {
            String line = "";
            String splitBy = ",";
            int j = 0; 
            int filecount = Integer.parseInt(args[1]); //number of frames that Hybond has outputted
            String rdg = Integer.parseInt(args[2]/10);
            List<String> vol = new ArrayList<String>();
            List<String> numpoints = new ArrayList<String>();
            List<String> kinEnergy = new ArrayList<String>();
            List<String> potEnergy = new ArrayList<String>(); //arrays to hold all the data
            List<String> totEnergy = new ArrayList<String>();
            List<String> elf = new ArrayList<String>();
            List<String> rho = new ArrayList<String>();
            int loops = Integer.parseInt(args[2]); //in the bonder style output the last column is the one wanted so this should be the rdg value * 10
            try
            {
                FileWriter csvWriter = new FileWriter(args[3]); //output file name 
                for(int i = 0; i < filecount; i++) //loops through all the files in the folder
                {
                    //System.out.println(i); 
                    String file = "";
                    file += args[0];    //file name setup
                    file += Integer.toString(i);
                    if(args[4].equals("m"))
                    {
                        file += "m.csv";
                    }
                    else if(args[4].equals("t")) //these condidtional statements help select with help of user input the type of file to be looked at
                    {                            //the m file is the minus interactions (attractive forces)   
                        file += "t.csv";         //the p file is the positive interactions (repulsive forces)
                    }                            //the t file the the total interactions (attractive + repulsive forces)
                    else 
                    {
                        file += "p.csv";
                    }
                    
                    File dir = new File(file); //create a file object which allows the following check as the program can fail to produce an output for a certain frame
                    if(dir.exists())
                    {
                        BufferedReader br = new BufferedReader(new FileReader(file)); //creation of the buffered reader
                        for(int k = 0; k < loops; k++) //loops to use up the lines in the file until the one that is wanted 
                        {
                            //System.out.println("loops: " + loops + " k: " + k);
                            if(br.readLine() == null) //takes lines out of the file that are unwanted
                            {
                                //System.out.println("Outputting na");
                            	kinEnergy.add("NA");	
                            	numpoints.add("NA");
                            	potEnergy.add("NA");
                            	totEnergy.add("NA");	
                            	vol.add("NA");
                            	elf.add("NA");	
                            	rho.add("NA");
                                //csvWriter.append(rdg + splitBy + "NA" + splitBy + "NA" + splitBy
                                    //+ "NA" + splitBy + "NA" + splitBy + "NA" + splitBy              //outputs Na to the file to show that the volume calculated was bad and therefore the energy can be assumed to follow the trend
                                      //  + "NA" + splitBy + "NA" + "\n");
                                break; //breaks out of the loop to avoid null pointers
                            }  
                        }

                        while((line = br.readLine()) != null) //reading the rest of the file in which there should only be one line left
                        {
         
                            String[] DataAr = line.split(splitBy); //using comma as a separator
                                                                   //index 0 = rdg val ** 1 = num points ** 2 = volume ** 3 = abr kinetic energy 
                                                                   // 4 = potential Energy ** 5 = total Energy ** 6 = elf ** 7 = rho
                            System.out.println(DataAr[0] + DataAr[1] + DataAr[2] + DataAr[3]);
                            rdg = DataAr[0];
                            numpoints.add(DataAr[1]);
                            vol.add(DataAr[2]);
                            kinEnergy.add(DataAr[3]);
                            potEnergy.add(DataAr[4]); //storing all the data
                            totEnergy.add(DataAr[5]);
                            elf.add(DataAr[6]);
                            rho.add(DataAr[7]);
                            //csvWriter.append(rdg + splitBy + numpoints.get(j) + splitBy + vol.get(j) + splitBy
                                //+ kinEnergy.get(j) + splitBy + potEnergy.get(j) + splitBy + totEnergy.get(j) + splitBy     //writing the data line to the file
                                  //  + elf.get(j) + splitBy + rho.get(j) + "\n");                                            //should only happen once as the last line of the file is the one wanted.
                            j++;                                                                                            //if a different rdg value is wanted change the cutoff value. 
                        }
                    }
                    else
                    {
                    	kinEnergy.add("0");	
                        numpoints.add("0");
                        potEnergy.add("0");
                        totEnergy.add("0");	
                        vol.add("0");
                        elf.add("0");	
                        rho.add("0");
                        //System.out.println("Outputting zero");
                       // csvWriter.append(rdg + splitBy + 0 + splitBy + 0 + splitBy
                         //   + 0 + splitBy + 0 + splitBy + 0 + splitBy               //this is called if Bonder did not make an output file for the interaction.
                           //     + 0 + splitBy + 0 + "\n");                          //not making a file occurs when the line is tested and its too long or the cutoff is past the threshold
                    }
                }
                List<String> Means = new ArrayList();
                Means = CalcMean(numpoints, vol, kinEnergy, potEnergy, totEnergy,elf,rho, filecount);
                CalcStandardDev(Means);
                AddMean();
                csvWriter.flush();
                csvWriter.close(); //closes the writer to stop IO errors
            }
            catch(IOException e) //catch to prevent bad IO errors
            {
                e.printStackTrace();
            }
        }
    }
        
}
