package ara.projet;

import ara.projet.mutex.NaimiTrehelAlgo;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EndControl implements Control {

  private static final String PAR_PROTO_APPLICATIF = "applicative";

  private final int pid_application;

  CsvWriter writer;

  private List<Double> metrique1 = new ArrayList<>();

  public EndControl(String prefix) {

    pid_application = Configuration.getPid(prefix + "." + PAR_PROTO_APPLICATIF);

    writer = new CsvWriter("test.csv");
  }


  @Override
  public boolean execute() {
    List<Integer> moyenneTimesRequesting = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("0.00");

    double nbReqTok = 0;
    double nbTokenTot = 0;
    double min1 = 0;
    double max1 = 0;


    double nbRequestTot = 0;
    double min2 = 0;
    double max2 = 0;

    double sumTime = 0;
    double sumNbCS = 0;


    int sumTokenUtilise = 0;
    int sumTokenInTransit = 0;

    for (int i = 0; i < Network.size(); i++) {
      Node node = Network.get(i);
      NaimiTrehelAlgo prot = (NaimiTrehelAlgo) node.getProtocol(pid_application);
      /** ******************** METRIQUE 3 **********************************/
      List<Integer> timeList = prot.getTimeList();
      if(timeList != null) {
        for (Integer time : timeList) {
          sumTime += time;

        }
        sumNbCS += prot.getNb_cs();
        //System.out.println("Node "+i+"  "+timeList.toString() + ", nb cs : " + prot.getNb_cs());
      }
      /** ------------------- FIN METRIQUE 3 ----------------------------- **/

      /** ******************** METRIQUE 2 **********************************/
      if(i==0){
        min2 = prot.getNbMessagesRequest();
        max2 = prot.getNbMessagesRequest();
      }else{
        if(min2 > prot.getNbMessagesRequest())
          min2 = prot.getNbMessagesRequest();
        if(max2 < prot.getNbMessagesRequest())
          max2 = prot.getNbMessagesRequest();
      }
      nbRequestTot += prot.getNbMessagesRequest();
      /** ------------------- FIN METRIQUE 2 ----------------------------- **/

      /** ******************** METRIQUE 1 **********************************/
      nbTokenTot += prot.getNbMessagesToken();

      nbReqTok = prot.getNbMessagesRequest() + prot.getNbMessagesToken();

      System.out.println("Node "+i+" nbReqTok = "+ nbReqTok);

      if(i==0){
        min1 = nbReqTok;
        max1 = nbReqTok;
      }else{
        if(min1 > nbReqTok)
          min1 = nbReqTok;
        if(max1 <nbReqTok)
          max1 = nbReqTok;
      }
      /** ------------------- FIN METRIQUE 1 ----------------------------- **/

      /** ******************** METRIQUE 4 **********************************/

      for(Integer t : prot.getListTimeTokenUtilise())
        sumTokenUtilise += t;
      //System.out.println("Node "+i+"  "+prot.getListTimeTokenInTransit().toString());
      for(Integer t : prot.getListTimeTokenInTransit())
        sumTokenInTransit += t;


      /** ------------------- FIN METRIQUE 4 ----------------------------- **/

      //System.out.println("Node "+i+ " a fini à t= "+ CommonState.getIntTime() + " et endTime = " +CommonState.getEndTime());

    }

    double m1 = (nbRequestTot+nbTokenTot)/ sumNbCS;
    double m2 = nbRequestTot/Network.size();
    double m3 = (sumTime/sumNbCS);


    System.out.println("METRIQUE 1 - Nombre de message applicatif par SC : " + (nbRequestTot+nbTokenTot)/ sumNbCS+
            "   min : "+min1 + " max : "+max1);
    System.out.println("METRIQUE 2 - Nombre de message request par noeud : " + nbRequestTot/Network.size()+
            "   min : "+min2 + " max : "+max2);
    System.out.println("METRIQUE 3 - Temps moyen pour obtenir la SC : "+ (sumTime/sumNbCS));//+ "     "+(sumTime/sumNbCS)/Network.size());
    double endtime = CommonState.getEndTime();
    //le pourcentage de temps que le jeton passe dans chacun de ses états (U, T et N)
    double U = (sumTokenUtilise/endtime) * 100.0;
    double T = (sumTokenInTransit/endtime) * 100.0;
    System.out.println("sumTokenUtilise = "+ sumTokenUtilise + "########## sumTokenInTransit = "+ sumTokenInTransit + " ###########   endtime = " +endtime);
    double timeNonUtilise = endtime - (sumTokenInTransit + sumTokenUtilise);
    double N = (timeNonUtilise / endtime) * 100.0;
    System.out.println("METRIQUE 4 - U ="+U +"   T = "+ T +"   N = "+N);

    String s = Metriques.timeBetweenCS+";"+ df.format(m1)+";" +df.format(m2)+";" +df.format(m3)+";" +df.format(U)+";" +df.format(T)+";" +df.format(N)+"\n";
    writer.write(s);
    return false;
  }
}