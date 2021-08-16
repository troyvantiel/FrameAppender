import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;
import java.util.Random;

public class FileProcess
{
	public static double volMean = 0;
	public static double numpointsMean = 0;
	public static double kinEnergyMean = 0;
	public static double potEnergyMean = 0;
	public static double totEnergyMean = 0;
	public static double elfMean = 0;
	public static double rhoMean = 0;



    public static int Random()
    {
        int randNum = 0;
        int upperbound = 201;
        Random rand = new Random();
        randNum = rand.nextInt(upperbound);
        //System.out.println("Random Number: " + randNum);
        return randNum;
    }

	public static List<List<String>> AddMean(List<List<String>> ValArray, List<Double> Mean, List<Float> STDev)
	{
	    for(int i =0; i < ValArray.size(); i++)
	    {
	        double UseMean = Mean.get(i);
	        System.out.println("Use mean: "+ UseMean);
	        double stndDev = STDev.get(i);
            System.out.println("Standard Deviation: "+ stndDev);
	        double lowEnd  = UseMean - (stndDev);
            for(int k = 0; k < ValArray.get(i).size(); k++)
            {
                    if(ValArray.get(i).get(k).equals("NA"))
                    {
                        double newR = Random();
                        newR = newR/100;
                        Double newVal = 0.0;
                        newVal = lowEnd + (stndDev * newR);
                        if(i == 0 ||i == 1 ||i == 2||i == 5)
                        {
                            if(newVal < 0)
                            {
                                newVal = newVal * -1;
                            }

                        }
                        if(i == 3)
                        {
                            if(newVal > 0)
                            {
                                System.out.println("New value before multiplication" + newVal);
                                newVal = newVal * -1;
                                System.out.println("New value after multiplication" + newVal);
                            }
                        }
                        if(i==0)
                        {
                            double roundNewVal = (double)newVal;
                            int iRoundNewVal = (int)roundNewVal;
                            String newValS = String.valueOf(iRoundNewVal);
                            ValArray.get(i).remove(k);
                            ValArray.get(i).add(k,newValS);
                        }
                        else
                        {
                            String newValS = newVal.toString();
                            ValArray.get(i).remove(k);
                            ValArray.get(i).add(k,newValS);
                        }
                    }
            }


        }

    return ValArray;

	}
    public static List<Double> CalcMean(List<String> numpoints, List<String> vol,List<String> kinEnergy,List<String> potEnergy,List<String> totEnergy,List<String> elf,List<String> rho, int div)
    {
        List<Double> Mean = new ArrayList<Double>();
        for(int k = 0; k < div; k++)
        {
            if(!vol.get(k).equals("NA"))
            {
                volMean += Double.parseDouble(vol.get(k));
                numpointsMean += Double.parseDouble(numpoints.get(k));
                kinEnergyMean += Double.parseDouble(kinEnergy.get(k));
                potEnergyMean += Double.parseDouble(potEnergy.get(k));
                totEnergyMean += Double.parseDouble(totEnergy.get(k));
                elfMean += Double.parseDouble(elf.get(k));
                rhoMean += Double.parseDouble(rho.get(k));
            }

        }
        Mean.add(numpointsMean/div);
        Mean.add(volMean/div);
        Mean.add(kinEnergyMean/div);
        Mean.add(potEnergyMean/div);
        Mean.add(totEnergyMean/div);
        Mean.add(elfMean/div);
        Mean.add(rhoMean/div);

        return Mean;
    }

	public static List<Float> CalcSTD(List<List<String>> ValArray, List<Double> Mean)
	{
		List<Float> Std = new ArrayList<Float>();
		Double StandardDeviation;

		for(int i = 0; i < ValArray.size(); i++)
		{
		    StandardDeviation = 0.0;
			for(String num: ValArray.get(i))
			{
			    if(!num.equals("NA"))
			    {
                    StandardDeviation += Math.pow(Float.parseFloat(num) - Mean.get(i), 2);
                    //System.out.println(StandardDeviation + " at point: " + track);
                }


			}
			Std.add((float) Math.sqrt(StandardDeviation/ValArray.get(i).size()));
		}
		return Std;
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
            String title = "";
            String splitBy = ",";
            boolean outputcheck = false;
            int j = 0; 
            int filecount = Integer.parseInt(args[1]); //number of frames that Hybond has outputted
            Integer rdgInt = Integer.parseInt(args[2])/10;
            String rdg1 = "";
            List<String> vol1 = new ArrayList<String>();
            List<String> numpoints1 = new ArrayList<String>();
            List<String> kinEnergy1 = new ArrayList<String>();
            List<String> potEnergy1 = new ArrayList<String>(); //arrays to hold all the data from rdg of 0.1
            List<String> totEnergy1 = new ArrayList<String>();
            List<String> elf1 = new ArrayList<String>();
            List<String> rho1 = new ArrayList<String>();

            String rdg2 = "";
            List<String> vol2 = new ArrayList<String>();
            List<String> numpoints2 = new ArrayList<String>();
            List<String> kinEnergy2 = new ArrayList<String>();
            List<String> potEnergy2 = new ArrayList<String>(); //arrays to hold all the data from rdg of 0.2
            List<String> totEnergy2 = new ArrayList<String>();
            List<String> elf2 = new ArrayList<String>();
            List<String> rho2 = new ArrayList<String>();

            String rdg3 = "";
            List<String> vol3 = new ArrayList<String>();
            List<String> numpoints3 = new ArrayList<String>();
            List<String> kinEnergy3 = new ArrayList<String>();
            List<String> potEnergy3 = new ArrayList<String>(); //arrays to hold all the data from rdg of 0.3
            List<String> totEnergy3 = new ArrayList<String>();
            List<String> elf3 = new ArrayList<String>();
            List<String> rho3 = new ArrayList<String>();

            String rdg4 = "";
            List<String> vol4 = new ArrayList<String>();
            List<String> numpoints4 = new ArrayList<String>();
            List<String> kinEnergy4 = new ArrayList<String>();
            List<String> potEnergy4 = new ArrayList<String>(); //arrays to hold all the data from rdg of 0.4
            List<String> totEnergy4 = new ArrayList<String>();
            List<String> elf4 = new ArrayList<String>();
            List<String> rho4 = new ArrayList<String>();

            String rdg5 =  "";
            List<String> vol5 = new ArrayList<String>();
            List<String> numpoints5 = new ArrayList<String>();
            List<String> kinEnergy5 = new ArrayList<String>();
            List<String> potEnergy5 = new ArrayList<String>(); //arrays to hold all the data from rdg of 0.5
            List<String> totEnergy5 = new ArrayList<String>();
            List<String> elf5 = new ArrayList<String>();
            List<String> rho5 = new ArrayList<String>();





            int loops = Integer.parseInt(args[2]); //in the bonder style output the last column is the one wanted so this should be the rdg value * 10
            try
            {
                String Filename = args[3];
                FileWriter csvWriter1 = new FileWriter(Filename + "1.csv");
                FileWriter csvWriter2 = new FileWriter(Filename + "2.csv");
                FileWriter csvWriter3 = new FileWriter(Filename + "3.csv");
                FileWriter csvWriter4 = new FileWriter(Filename + "4.csv");
                FileWriter csvWriter5 = new FileWriter(Filename + "5.csv");//output file name
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
                    System.out.println(file);
                    if(dir.exists())
                    {
                        outputcheck = false;
                        BufferedReader br = new BufferedReader(new FileReader(file)); //creation of the buffered reader
                        title = br.readLine();
                        System.out.println("Title line is as follows: " + title);
                        for(int k = 0; k <= loops; k++) //loops to use up the lines in the file until the one that is wanted
                        {
                            //System.out.println("loops: " + loops + " k: " + k);
                            line = br.readLine();
                            System.out.println("Line contains: " + line);
                            if((line == null|| line.contains("na"))) //takes lines out of the file that are unwanted
                            {
                                System.out.println("Outputting na");
                                kinEnergy1.add("NA");
                                numpoints1.add("NA");
                                potEnergy1.add("NA");
                                totEnergy1.add("NA");
                                vol1.add("NA");
                                elf1.add("NA");
                                rho1.add("NA");

                                kinEnergy2.add("NA");
                                numpoints2.add("NA");
                                potEnergy2.add("NA");
                                totEnergy2.add("NA");
                                vol2.add("NA");
                                elf2.add("NA");
                                rho2.add("NA");

                                kinEnergy3.add("NA");
                                numpoints3.add("NA");
                                potEnergy3.add("NA");   //have to add NA to all arrays if there is no data so that the program can add in data
                                totEnergy3.add("NA");
                                vol3.add("NA");
                                elf3.add("NA");
                                rho3.add("NA");

                                kinEnergy4.add("NA");
                                numpoints4.add("NA");
                                potEnergy4.add("NA");
                                totEnergy4.add("NA");
                                vol4.add("NA");
                                elf4.add("NA");
                                rho4.add("NA");

                                kinEnergy5.add("NA");
                                numpoints5.add("NA");
                                potEnergy5.add("NA");
                                totEnergy5.add("NA");
                                vol5.add("NA");
                                elf5.add("NA");
                                rho5.add("NA");
                                //csvWriter.append(rdg + splitBy + "NA" + splitBy + "NA" + splitBy
                                //+ "NA" + splitBy + "NA" + splitBy + "NA" + splitBy              //outputs Na to the file to show that the volume calculated was bad and therefore the energy can be assumed to follow the trend
                                //  + "NA" + splitBy + "NA" + "\n");
                                outputcheck = true;
                                break; //breaks out of the loop to avoid null pointers
                            }
                            else
                            {
                                //Split the data of each line and store them for each rdg value
                                String[] DataAr = line.split(splitBy);
                                outputcheck = true;
                                System.out.println("Outputting data to the files");
                                if(k == 0)
                                {
                                    System.out.println("outputting to 0.1");
                                    rdg1 = DataAr[0];
                                    numpoints1.add(DataAr[1]);
                                    vol1.add(DataAr[2]);
                                    kinEnergy1.add(DataAr[3]);
                                    potEnergy1.add(DataAr[4]); //storing all the data
                                    totEnergy1.add(DataAr[5]);
                                    elf1.add(DataAr[6]);
                                    rho1.add(DataAr[7]);
                                }
                                else if(k == 1)
                                {
                                    System.out.println("outputting to 0.2");
                                    rdg2 = DataAr[0];
                                    numpoints2.add(DataAr[1]);
                                    vol2.add(DataAr[2]);
                                    kinEnergy2.add(DataAr[3]);
                                    potEnergy2.add(DataAr[4]); //storing all the data
                                    totEnergy2.add(DataAr[5]);
                                    elf2.add(DataAr[6]);
                                    rho2.add(DataAr[7]);
                                }
                                else if(k == 2)
                                {
                                    System.out.println("outputting to 0.3");
                                    rdg3 = DataAr[0];
                                    numpoints3.add(DataAr[1]);
                                    vol3.add(DataAr[2]);
                                    kinEnergy3.add(DataAr[3]);
                                    potEnergy3.add(DataAr[4]); //storing all the data
                                    totEnergy3.add(DataAr[5]);
                                    elf3.add(DataAr[6]);
                                    rho3.add(DataAr[7]);
                                }
                                else if(k == 3)
                                {
                                    System.out.println("outputting to 0.4");
                                    rdg4 = DataAr[0];
                                    numpoints4.add(DataAr[1]);
                                    vol4.add(DataAr[2]);
                                    kinEnergy4.add(DataAr[3]);
                                    potEnergy4.add(DataAr[4]); //storing all the data
                                    totEnergy4.add(DataAr[5]);
                                    elf4.add(DataAr[6]);
                                    rho4.add(DataAr[7]);
                                }
                                else if(k == 4)
                                {
                                    System.out.println("outputting to 0.5");
                                    rdg5 = DataAr[0];
                                    numpoints5.add(DataAr[1]);
                                    vol5.add(DataAr[2]);
                                    kinEnergy5.add(DataAr[3]);
                                    potEnergy5.add(DataAr[4]); //storing all the data
                                    totEnergy5.add(DataAr[5]);
                                    elf5.add(DataAr[6]);
                                    rho5.add(DataAr[7]);
                                }



                            }
                        }
                        /*
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
                        }*/
                    }
                    else
                    {
                        System.out.println("###########OUTPUTTING ZEROS###############");
                    	kinEnergy1.add("0");
                        numpoints1.add("0");
                        potEnergy1.add("0");
                        totEnergy1.add("0");
                        vol1.add("0");
                        elf1.add("0");
                        rho1.add("0");

                        kinEnergy2.add("0");
                        numpoints2.add("0");
                        potEnergy2.add("0");
                        totEnergy2.add("0");
                        vol2.add("0");
                        elf2.add("0");
                        rho2.add("0");

                        kinEnergy3.add("0");
                        numpoints3.add("0");
                        potEnergy3.add("0");
                        totEnergy3.add("0");
                        vol3.add("0");
                        elf3.add("0");
                        rho3.add("0");

                        kinEnergy4.add("0");
                        numpoints4.add("0");
                        potEnergy4.add("0");
                        totEnergy4.add("0");
                        vol4.add("0");
                        elf4.add("0");
                        rho4.add("0");

                        kinEnergy5.add("0");
                        numpoints5.add("0");
                        potEnergy5.add("0");
                        totEnergy5.add("0");
                        vol5.add("0");
                        elf5.add("0");
                        rho5.add("0");

                        //System.out.println("Outputting zero");
                       // csvWriter.append(rdg + splitBy + 0 + splitBy + 0 + splitBy
                         //   + 0 + splitBy + 0 + splitBy + 0 + splitBy               //this is called if Bonder did not make an output file for the interaction.
                           //     + 0 + splitBy + 0 + "\n");                          //not making a file occurs when the line is tested and its too long or the cutoff is past the threshold
                    }
                }
                List<List<String>> ValArray1 = new ArrayList<List<String>>();
                List<List<String>> ValArray2 = new ArrayList<List<String>>();
                List<List<String>> ValArray3 = new ArrayList<List<String>>();
                List<List<String>> ValArray4 = new ArrayList<List<String>>();
                List<List<String>> ValArray5 = new ArrayList<List<String>>();

                ValArray1.add(numpoints1);
                ValArray1.add(vol1);
                ValArray1.add(kinEnergy1);
                ValArray1.add(potEnergy1);
                ValArray1.add(totEnergy1);
                ValArray1.add(elf1);
                ValArray1.add(rho1);

                ValArray2.add(numpoints2);
                ValArray2.add(vol2);
                ValArray2.add(kinEnergy2);
                ValArray2.add(potEnergy2);
                ValArray2.add(totEnergy2);
                ValArray2.add(elf2);
                ValArray2.add(rho2);

                ValArray3.add(numpoints3);
                ValArray3.add(vol3);
                ValArray3.add(kinEnergy3);
                ValArray3.add(potEnergy3);
                ValArray3.add(totEnergy3);
                ValArray3.add(elf3);
                ValArray3.add(rho3);

                ValArray4.add(numpoints4);
                ValArray4.add(vol4);
                ValArray4.add(kinEnergy4);
                ValArray4.add(potEnergy4);
                ValArray4.add(totEnergy4);
                ValArray4.add(elf4);
                ValArray4.add(rho4);

                ValArray5.add(numpoints5);
                ValArray5.add(vol5);
                ValArray5.add(kinEnergy5);
                ValArray5.add(potEnergy5);
                ValArray5.add(totEnergy5);
                ValArray5.add(elf5);
                ValArray5.add(rho5);

                List<Double> Means1 = new ArrayList<Double>();
                List<Float> StandardDeviationArray1 = new ArrayList<Float>();
                List<List<String>> FinalData1 = new ArrayList<List<String>>();

                List<Double> Means2 = new ArrayList<Double>();
                List<Float> StandardDeviationArray2 = new ArrayList<Float>();
                List<List<String>> FinalData2 = new ArrayList<List<String>>();

                List<Double> Means3 = new ArrayList<Double>();
                List<Float> StandardDeviationArray3 = new ArrayList<Float>();
                List<List<String>> FinalData3 = new ArrayList<List<String>>();

                List<Double> Means4 = new ArrayList<Double>();
                List<Float> StandardDeviationArray4 = new ArrayList<Float>();
                List<List<String>> FinalData4 = new ArrayList<List<String>>();

                List<Double> Means5 = new ArrayList<Double>();
                List<Float> StandardDeviationArray5 = new ArrayList<Float>();
                List<List<String>> FinalData5 = new ArrayList<List<String>>();

                Means1 = CalcMean(numpoints1, vol1, kinEnergy1, potEnergy1, totEnergy1,elf1,rho1, filecount);
                Means2 = CalcMean(numpoints2, vol2, kinEnergy2, potEnergy2, totEnergy2,elf2,rho2, filecount);
                Means3 = CalcMean(numpoints3, vol3, kinEnergy3, potEnergy3, totEnergy3,elf3,rho3, filecount);
                Means4 = CalcMean(numpoints4, vol4, kinEnergy4, potEnergy4, totEnergy4,elf4,rho4, filecount);
                Means5 = CalcMean(numpoints5, vol5, kinEnergy5, potEnergy5, totEnergy5,elf5,rho5, filecount);

                StandardDeviationArray1 = CalcSTD(ValArray1, Means1);
                StandardDeviationArray2 = CalcSTD(ValArray2, Means2);
                StandardDeviationArray3 = CalcSTD(ValArray3, Means3);
                StandardDeviationArray4 = CalcSTD(ValArray4, Means4);
                StandardDeviationArray5 = CalcSTD(ValArray5, Means5);

                FinalData1 = AddMean(ValArray1,Means1,StandardDeviationArray1);
                FinalData2 = AddMean(ValArray2,Means2,StandardDeviationArray2);
                FinalData3 = AddMean(ValArray3,Means3,StandardDeviationArray3);
                FinalData4 = AddMean(ValArray4,Means4,StandardDeviationArray4);
                FinalData5 = AddMean(ValArray5,Means5,StandardDeviationArray5);

                for(int y = 0; y < FinalData1.get(0).size(); y++)
                {
                    csvWriter1.append(rdg1 + splitBy + FinalData1.get(0).get(y) + splitBy + FinalData1.get(1).get(y) + splitBy
                    + FinalData1.get(2).get(y) + splitBy + FinalData1.get(3).get(y) + splitBy + FinalData1.get(4).get(y) + splitBy     //writing the data line to the file
                      + FinalData1.get(5).get(y) + splitBy + FinalData1.get(6).get(y) + "\n");
                }

                for(int y = 0; y < FinalData2.get(0).size(); y++)
                {
                    csvWriter2.append(rdg2 + splitBy + FinalData2.get(0).get(y) + splitBy + FinalData2.get(1).get(y) + splitBy
                            + FinalData2.get(2).get(y) + splitBy + FinalData2.get(3).get(y) + splitBy + FinalData2.get(4).get(y) + splitBy     //writing the data line to the file
                            + FinalData2.get(5).get(y) + splitBy + FinalData2.get(6).get(y) + "\n");
                }

                for(int y = 0; y < FinalData3.get(0).size(); y++)
                {
                    csvWriter3.append(rdg3 + splitBy + FinalData3.get(0).get(y) + splitBy + FinalData3.get(1).get(y) + splitBy
                            + FinalData3.get(2).get(y) + splitBy + FinalData3.get(3).get(y) + splitBy + FinalData3.get(4).get(y) + splitBy     //writing the data line to the file
                            + FinalData3.get(5).get(y) + splitBy + FinalData3.get(6).get(y) + "\n");
                }

                for(int y = 0; y < FinalData4.get(0).size(); y++)
                {
                    csvWriter4.append(rdg4 + splitBy + FinalData4.get(0).get(y) + splitBy + FinalData4.get(1).get(y) + splitBy
                            + FinalData4.get(2).get(y) + splitBy + FinalData4.get(3).get(y) + splitBy + FinalData4.get(4).get(y) + splitBy     //writing the data line to the file
                            + FinalData4.get(5).get(y) + splitBy + FinalData4.get(6).get(y) + "\n");
                }

                for(int y = 0; y < FinalData5.get(0).size(); y++)
                {
                    csvWriter5.append(rdg5 + splitBy + FinalData5.get(0).get(y) + splitBy + FinalData5.get(1).get(y) + splitBy
                            + FinalData5.get(2).get(y) + splitBy + FinalData5.get(3).get(y) + splitBy + FinalData5.get(4).get(y) + splitBy     //writing the data line to the file
                            + FinalData5.get(5).get(y) + splitBy + FinalData5.get(6).get(y) + "\n");
                }


                csvWriter1.flush();
                csvWriter1.close(); //closes the writer to stop IO errors

                csvWriter2.flush();
                csvWriter2.close();

                csvWriter3.flush();
                csvWriter3.close();

                csvWriter4.flush();
                csvWriter4.close();

                csvWriter5.flush();
                csvWriter5.close();

            }
            catch(IOException e) //catch to prevent bad IO errors
            {
                e.printStackTrace();
            }
        }
    }
        
}
