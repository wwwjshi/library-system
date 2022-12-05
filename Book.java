import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Book {
    // attributes
    private String title;
    private String author;
    private String genre;
    private String serialNumber;
    private boolean isRented;
    private String renterNumber;
    private List<Member> renterHistory = new ArrayList<Member>();

    // constructor
    public Book(String title, String author, String genre, String serialNumber){
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.serialNumber = serialNumber;
    }

    // instance methods
    public String getTitle(){
        return this.title;
    }

    public String getAuthor(){
        return this.author;
    }

    public String getGenre(){
        return this.genre;
    }

    public String getSerialNumber(){
        return this.serialNumber;
    }

    public String longString(){
        String arg5 = "Currently available"; 
        if (this.isRented){
            arg5 = "Rented by: " + this.renterNumber;
        }
        String longStr = String.format("%1$s: %2$s (%3$s, %4$s)\n%5$s.", this.serialNumber, this.title, this.author, this.genre, arg5);
        //String longStr =  this.serialNumber + ": " + this.title + " (" + this.author + ", " + this.genre + ")" + "\n" + arg5 + ".";
        return longStr;
    }

    public String shortString(){
        String shortStr = this.title + " (" + this.author + ")";
        return shortStr;
    }

    public List<Member> renterHistory(){
        return this.renterHistory;
    }

    public boolean isRented(){
        return this.isRented;
    }
    
    public boolean rent(Member member){
        if(member == null){
            return false;
        }
        if(this.isRented){
            return false;
        }
        this.isRented = true;
        this.renterNumber = member.getMemberNumber();
        return true;
    }

    public boolean relinquish(Member member){
        if(member == null || member.getMemberNumber() != this.renterNumber){
            return false;
        }
        this.isRented = false;
        this.renterNumber = null;
        this.renterHistory.add(member);
        return true;

    }

    // Static method
    public static Book readBook(String filename, String serialNumber){
        if(filename == null || serialNumber == null){
            return null;
        }
        try{
            File f = new File(filename);
            Scanner scan = new Scanner(f);
            while(scan.hasNextLine()){
                boolean matchFound = true;
                String[] strArr = scan.nextLine().split(",");
                String serialNum = strArr[0];
                // compare serial number
                if(serialNum.length() != serialNumber.length()){
                    matchFound = false;
                    continue;
                }
                int i = 0;
                while(i < serialNum.length()){
                    if(serialNum.charAt(i) != serialNumber.charAt(i)){
                        matchFound = false;
                        break;
                    }
                    i += 1;
                }
                // serial number found
                if(matchFound){
                    Book newBook = new Book(strArr[1], strArr[2], strArr[3], serialNum);
                    return newBook; 
                }
            }
            // no match serial number found
            return null; 
        // no file found
        } catch(FileNotFoundException e){
            return null;
        }
    }

    public static List<Book> readBookCollection(String filename){
        if (filename == null){
            return null;
        }
        try{
            File f = new File(filename);
            Scanner sc = new Scanner(f);
            ArrayList<String[]> slist = new ArrayList<String[]>();
            while(sc.hasNextLine()){
                String[] strArr = sc.nextLine().split(",");
                if(strArr.length == 4){
                    slist.add(strArr);
                }
            }
            // no book info in file
            if(slist.size() < 2){
                return null;
            }
            // book list to add and return
            List<Book> bookList = new ArrayList<Book>();
            int i = 1;
            while (i < slist.size()){
                String[] bStr = slist.get(i);
                Book booki = new Book(bStr[1], bStr[2], bStr[3], bStr[0]);
                bookList.add(booki);
                i += 1;
            }
            return bookList;
        // no file found
        } catch(FileNotFoundException e){
            return null;
        }
    }

    public static void saveBookCollection(String filename, Collection<Book> books){
        if(filename == null || books == null || books.isEmpty()){
            return;
        }
        Book[] bookArr = books.toArray(new Book[books.size()]);
        try{
            File f = new File(filename);
            PrintWriter w = new PrintWriter(f);
            w.println("serialNumber,title,author,genre");
            int i = 0;
            while(i < bookArr.length){
                Book booki = bookArr[i];
                if (booki == null){
                    i += 1;
                    continue;
                }
                String s = String.join(",", booki.serialNumber, booki.title, booki.author, booki.genre);
                w.println(s);
                i += 1;
            }
            w.flush();
            w.close();
        } catch (FileNotFoundException e){
            return;
        }
    }

    public static List<Book> filterAuthor(List<Book> books, String author){
        if (books == null || author == null){
            return null;
        }
        List<Book> abooklist = new ArrayList<Book>();
        for(Book b : books){
            if(b == null){
                continue;
            }
            if( b.author.equals(author) ){
                abooklist.add(b);
            }
        }
        return abooklist;
    }

    public static List<Book> filterGenre(List<Book> books, String genre){
        if (books == null || genre == null){
            return null;
        }
        List<Book> gbooklist = new ArrayList<Book>();
        for(Book b : books){
            if(b == null){
                continue;
            }
            if( b.genre.equals(genre) ){
                gbooklist.add(b);
            }
        }
        return gbooklist;
    }

    public int getIntSerialNumber(){
        return Integer.parseInt(this.serialNumber);
    }
}
