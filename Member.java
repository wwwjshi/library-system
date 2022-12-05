import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Member {
    // attributes
    private String name;
    private String memberNumber;
    private List<Book> rentingList = new ArrayList<Book>();
    private List<Book> rentedHistory = new ArrayList<Book>();

    // constructor
    public Member(String name, String memberNumber){
        this.name = name;
        this.memberNumber = memberNumber;
    }

    // instance method
    public String getName(){
        return this.name;
    }

    public String getMemberNumber(){
        return this.memberNumber;
    }

    public boolean rent(Book book){
        if(book == null){
            return false;
        }
        boolean rentSuc = book.rent(this);
        if(rentSuc){
            this.rentingList.add(book);
            return true;
        }
        return false;
    }

    public boolean relinquish(Book book){
        if (book == null){
            return false;
        }
        boolean relinq = book.relinquish(this);
        if(relinq){
            this.rentedHistory.add(book);
            this.rentingList.remove(book);
        }
        return relinq;
    }

    public void relinquishAll(){
        if (this.rentingList == null || this.rentingList.isEmpty()){
            return;
        }
        for(Book b : this.rentingList){
            boolean relinq = b.relinquish(this);
            this.rentedHistory.add(b);
        }
        this.rentingList = new ArrayList<Book>();;
    }

    public List<Book> history(){
        return this.rentedHistory;
    }

    public List<Book> renting(){
        return this.rentingList;
    }

    public static List<Book> commonBooks(Member[] members){
        if(members == null || members.length == 0){
            return null;
        }
        for(Member m: members){
            if(m == null){
                return null;
            }
        }
        HashMap<Book, Integer> countBook = new HashMap<Book, Integer>();
        for(Book b : members[0].history()){
            if(countBook.get(b) != null){
                continue;
            }
            countBook.put(b, 0);
            for(Member m : members){
                for(Member mb : b.renterHistory()){
                    if(m.equals(mb)){
                        countBook.replace(b, countBook.get(b) + 1);
                        break;
                    }
                }
            }
        }
        List<Book> combook = new ArrayList<Book>();
        HashMap<Book, Integer> tempMap = new HashMap<Book, Integer>();
        for(Book b1 : countBook.keySet()){
            if(countBook.get(b1) >= members.length){
                tempMap.put(b1, Integer.parseInt(b1.getSerialNumber()));
                combook.add(b1);
            }
        }
        // no common book
        if(tempMap.isEmpty()){
            return combook;
        }
        // sort
        List<Book> temp = new ArrayList<Book>();
        while(combook.isEmpty() == false){
            Book minSer = combook.get(0);
            int minVal = tempMap.get(minSer);
            int j = 1;
            while(j < combook.size()){
                if(tempMap.get(combook.get(j)) < minVal){
                    minSer = combook.get(j);
                    minVal = tempMap.get(minSer);
                }
                j += 1;
            }
            temp.add(minSer);
            combook.remove(minSer);
        }
        combook = temp;
        return combook;
    }
}
