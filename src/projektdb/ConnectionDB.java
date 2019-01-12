package projektdb;


import klasy.Artykul;
import klasy.Klient;
import klasy.Sprzedawca;
import klasy.Sprzedaz;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

public class ConnectionDB{

    private final String url = "hibernate.cfg.xml";
    private Configuration configuration = null;
    private SessionFactory factory= null;
    Session session = null;
    private Transaction transaction=null;



    public ConnectionDB() {
        this.configuration = new Configuration().configure(url);

        this.configuration.addAnnotatedClass(Sprzedawca.class);
        this.configuration.addAnnotatedClass(Klient.class);
        this.configuration.addAnnotatedClass(Sprzedaz.class);
        this.configuration.addAnnotatedClass(Artykul.class);
      

        this.factory = this.configuration.buildSessionFactory();
        this.session = this.factory.openSession();
        this.transaction = this.session.beginTransaction();
    }

    public void closeConnectionWithTransaction(){
        this.transaction.commit();
        this.session.close();
        this.factory.close();
    }
    
    public void closeConnectionWithOutTransaction(){
        this.session.close();
        this.factory.close();
    }
    
    public void addSprzedawca(Sprzedawca sprzedawca){
        this.session.save(sprzedawca);
    }
    
    public void addArtykul(Artykul artykul){
        this.session.save(artykul);
    }

    public void addKlient(Klient klient){
        this.session.save(klient);
    }
    
    public void addSprzedaz(Sprzedaz sprzedaz){
        this.session.save(sprzedaz);
    }
    
    public void deleteSprzedawca(Sprzedawca sprzedawca){
        Criteria criteria = this.session.createCriteria(Sprzedawca.class);
        criteria.add(Restrictions.eq("imie",sprzedawca.getImie()));
        criteria.add(Restrictions.and(Restrictions.eq("nazwisko",sprzedawca.getNazwisko())));
        criteria.add(Restrictions.and(Restrictions.eq("pensja", sprzedawca.getPensja())));
        
        Sprzedawca sprzedawca1 = (Sprzedawca) criteria.uniqueResult();
        try {
            this.session.delete(sprzedawca1);
            System.out.println("Sprzedawca usuniety");
        } catch (Exception e) {
            System.out.println("Blad usuwania");
            e.printStackTrace();
        }
    }
    
    public void deleteKlient(Klient klient){
        Criteria criteria = this.session.createCriteria(Klient.class);
        criteria.add(Restrictions.eq("imie",klient.getImie()));
        criteria.add(Restrictions.and(Restrictions.eq("nazwisko",klient.getNazwisko())));
        Klient klient1 = (Klient) criteria.uniqueResult();
        try {
            this.session.delete(klient1);
            System.out.println("Klient usuniety");
        } catch (Exception e) {
            System.out.println("Blad usuwania");
            e.printStackTrace();
        }
        
    }
    
   public void deleteArtykul(Artykul artykul){
        Criteria criteria = this.session.createCriteria(Artykul.class);
        criteria.add(Restrictions.eq("nazwa",artykul.getNazwa()));
        criteria.add(Restrictions.and(Restrictions.eq("cena",artykul.getCena())));
        Artykul artykul1 = (Artykul) criteria.uniqueResult();
        try {
            this.session.delete(artykul1);
            System.out.println("Artykul usuniety");
        } catch (Exception e) {
            System.out.println("Blad usuwania");
            e.printStackTrace();
        } 
       
   }
   
   public void makeSell(Sprzedawca sprzedawca,Klient klient,Artykul artykul){
       Criteria criteriaSprzedawca = this.session.createCriteria(Sprzedawca.class);
        criteriaSprzedawca.add(Restrictions.eq("imie",sprzedawca.getImie()));
        criteriaSprzedawca.add(Restrictions.and(Restrictions.eq("nazwisko",sprzedawca.getNazwisko())));
        criteriaSprzedawca.add(Restrictions.and(Restrictions.eq("pensja", sprzedawca.getPensja())));
        
        Criteria criteriaKlient = this.session.createCriteria(Klient.class);
        criteriaKlient.add(Restrictions.eq("imie",klient.getImie()));
        criteriaKlient.add(Restrictions.and(Restrictions.eq("nazwisko",klient.getNazwisko())));
        
        Criteria criteriaArtykul = this.session.createCriteria(Artykul.class);
        criteriaArtykul.add(Restrictions.eq("nazwa",artykul.getNazwa()));
        criteriaArtykul.add(Restrictions.and(Restrictions.eq("cena",artykul.getCena())));
        
        Sprzedawca sprzedawca1 = (Sprzedawca) criteriaSprzedawca.uniqueResult();
        Klient klient1 = (Klient) criteriaKlient.uniqueResult();
        Artykul artykul1 = (Artykul) criteriaArtykul.uniqueResult();
        
        if (!sprzedawca1.equals(null)) {
            if (!klient1.equals(null)) {
                if (!artykul1.equals(null)) {
                    Sprzedaz sprzedaz = new Sprzedaz();
                    sprzedaz.setSprzedawca(sprzedawca1);
                    sprzedaz.setKlient(klient1);
                    sprzedaz.setArtykul(artykul1);
                    if (!sprzedaz.equals(null)) {
                        this.session.save(sprzedaz);
                        System.out.println("Dodano Sprzedaz");
                    }
                }else{
                    System.out.println("Bledny artykul");
                }
                
            }else{
                System.out.println("Bledny klient");
            }
           
       }else{
            System.out.println("Bledny sprzedawca");
        }
   }
}