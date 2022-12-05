import java.util.HashMap;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Scanner;
import java.util.Set;

public class Library {
    // attributes
    public static final String HELP_STRING = "EXIT ends the library process\n" + 
                                             "COMMANDS outputs this help string\n\n" + 
                                             "LIST ALL [LONG] outputs either the short or long string for all books\n" +
                                             "LIST AVAILABLE [LONG] outputs either the short of long string for all available books\n" +
                                             "NUMBER COPIES outputs the number of copies of each book\n" +
                                             "LIST GENRES outputs the name of every genre in the system\n" + 
                                             "LIST AUTHORS outputs the name of every author in the system\n\n" + 
                                             "GENRE <genre> outputs the short string of every book with the specified genre\n" + 
                                             "AUTHOR <author> outputs the short string of every book by the specified author\n\n" + 
                                             "BOOK <serialNumber> [LONG] outputs either the short or long string for the specified book\n" + 
                                             "BOOK HISTORY <serialNumber> outputs the rental history of the specified book\n\n" + 
                                             "MEMBER <memberNumber> outputs the information of the specified member\n" + 
                                             "MEMBER BOOKS <memberNumber> outputs the books currently rented by the specified member\n" +
                                             "MEMBER HISTORY <memberNumber> outputs the rental history of the specified member\n\n" +
                                             "RENT <memberNumber> <serialNumber> loans out the specified book to the given member\n" + 
                                             "RELINQUISH <memberNumber> <serialNumber> returns the specified book from the member\n" +
                                             "RELINQUISH ALL <memberNumber> returns all books rented by the specified member\n\n" +
                                             "ADD MEMBER <name> adds a member to the system\n" +
                                             "ADD BOOK <filename> <serialNumber> adds a book to the system\n\n" +
                                             "ADD COLLECTION <filename> adds a collection of books to the system\n" +
                                             "SAVE COLLECTION <filename> saves the system to a csv file\n\n" +
                                             "COMMON <memberNumber1> <memberNumber2> ... outputs the common books in members\' history";
    private ArrayList<Book> books = new ArrayList<Book>();
    private ArrayList<Member> members = new ArrayList<Member>();

    // Constructor
    public Library(){
    }

    // methods
    public void getAllBooks(boolean fullString){
        if(this.books == null || this.books.size() == 0){
            System.out.println("No books in system.\n");
            return;
        }
        for(Book b : this.books){
            String toPrint = b.shortString();
            if(fullString){
                toPrint = b.longString() + "\n";
            }
            System.out.println(toPrint);
        }
        if(fullString == false){
            System.out.println();
        }
    }

    public void getAvailableBooks(boolean fullString){
        if(this.books == null || this.books.isEmpty()){
            System.out.println("No books in system.\n");
            return;
        }
        int count = 0;
        for(Book b : this.books){
            if(!b.isRented()){
                String toPrint = b.shortString();
                if(fullString){
                    toPrint = b.longString() + "\n";
                }
                System.out.println(toPrint);
                count += 1;
            }
        }
        if(count == 0){
            System.out.println("No books available.\n");
            return;
        }
        if(fullString == false){
            System.out.println();
        }
    }

    public void getCopies(){
        if(this.books == null || this.books.isEmpty()){
            System.out.println("No books in system.");
            return;
        }
        HashMap<Book, Integer> bookMap = new HashMap<Book, Integer>();
        for(Book b: this.books){
            boolean sameBookFound = false;
            Book curBook = b;
            for(Book bm: bookMap.keySet()){
                if(b.shortString().equals(bm.shortString())){
                    sameBookFound = true;
                    curBook = bm;
                    break;
                }
            }
            if(sameBookFound){
                bookMap.replace(curBook, bookMap.get(curBook)+1);
            } else{
                bookMap.put(b, 1);
            }
        }
        Set<Book> bmKeys = bookMap.keySet();
        ArrayList<Book> booklist = new ArrayList<Book>(bmKeys);
        booklist.sort((x, y) -> x.getTitle().compareTo(y.getTitle()));
        for(Book b : booklist){
            String toPrint = b.shortString() + ": " + bookMap.get(b);
            System.out.println(toPrint);
        }
    }

    public void getGenres(){
        if(this.books == null || this.books.isEmpty()){
            System.out.println("No books in system.");
            return;
        }
        ArrayList<String> genrelist = new ArrayList<String>();
        for(Book b : this.books){
            if(genrelist.indexOf(b.getGenre()) == -1){
                genrelist.add(b.getGenre());
            }
        }
        genrelist.sort((x, y) -> x.compareTo(y));
        for(String g: genrelist){
            System.out.println(g);
        }
    }

    public void getAuthors(){
        if(this.books == null || this.books.isEmpty()){
            System.out.println("No books in system.");
            return;
        }
        ArrayList<String> authorlist = new ArrayList<String>();
        for(Book b : this.books){
            if(authorlist.indexOf(b.getAuthor()) == -1){
                authorlist.add(b.getAuthor());
            }
        }
        authorlist.sort((x, y) -> x.compareTo(y));
        for(String a: authorlist){
            System.out.println(a);
        }
    }

    public void getBooksByGenre(String genre){
        if(this.books == null || this.books.isEmpty()){
            System.out.println("No books in system.");
            return;
        }
        List<Book> gfilterls = Book.filterGenre(this.books, genre);
        if(gfilterls == null || gfilterls.size() == 0){
            System.out.println("No books with genre " + genre + ".");
            return;
        } 
        //sort
        gfilterls.sort((x, y) -> x.getSerialNumber().compareTo(y.getSerialNumber()));
        for(Book b: gfilterls){
            System.out.println(b.shortString());
        }
    }

    public void getBooksByAuthor(String author){
        if(this.books == null || this.books.isEmpty()){
            System.out.println("No books in system.");
            return;
        }
        List<Book> afilterls = Book.filterAuthor(this.books, author);
        if(afilterls == null || afilterls.size() == 0){
            System.out.println("No books by " + author + ".");
            return;
        } 
        //sort
        afilterls.sort((x, y) -> x.getAuthor().compareTo(y.getAuthor()));
        for(Book b: afilterls){
            System.out.println(b.shortString());
        }
    }

    public void getBook(String serialNumber, boolean fullString){
        if(this.books == null || this.books.isEmpty()){
            System.out.println("No books in system.");
            return;
        }
        if(serialNumber == null){
            System.out.println("No such book in system.");
            return;
        }
        for(Book b: this.books){
            if(b.getSerialNumber().equals(serialNumber)){
                if(fullString){
                    System.out.println(b.longString());
                } else {
                    System.out.println(b.shortString());
                }
                return;
            }
        }
        System.out.println("No such book in system.");
    }

    public void bookHistory(String serialNumber){
        if(serialNumber == null){
            System.out.println("No such book in system.");
            return;
        }
        List<Member> rHistory = new ArrayList<Member>();
        boolean found = false;
        for(Book b: this.books){
            if(b.getSerialNumber().equals(serialNumber)){
                rHistory = b.renterHistory();
                found = true;
            }
        }
        if(found == false){
            System.out.println("No such book in system.");
            return; 
        }
        if(rHistory == null || rHistory.isEmpty()){
            System.out.println("No rental history.");
            return;
        }
        for(Member m : rHistory){
            System.out.println(m.getMemberNumber());
        }
    }

    public void addBook(String bookFile, String serialNumber){
        if(bookFile == null){
            System.out.println("No such file.");
            return;
        }
        File f = new File(bookFile);
        if(f.exists() == false){
            System.out.println("No such file.");
            return;
        }
        for(Book b: this.books){
            if(b.getSerialNumber().equals(serialNumber)){
                System.out.println("Book already exists in system.");
                return;
            }
        }
        Book newbook = Book.readBook(bookFile, serialNumber);
        if(newbook == null){
            System.out.println( "No such book in file.");
            return;
        }
        this.books.add(newbook);
        System.out.println("Successfully added: " + newbook.shortString() + ".");
    }

    public void rentBook(String memberNumber, String serialNumber){
        if(this.members == null || this.members.isEmpty()){
            System.out.println("No members in system.");
            return;
        }
        if(this.books == null || this.books.isEmpty()){
            System.out.println("No books in system.");
            return;
        }
        if(memberNumber == null){
            System.out.println("No such member in system.");
            return;
        }
        Member renter = this.members.get(0);
        boolean memberFound = false;
        for(Member m: this.members){
            if(m.getMemberNumber().equals(memberNumber)){
                memberFound = true;
                renter = m;
                break;
            }
        }
        if(memberFound == false){
            System.out.println("No such member in system.");
            return;
        }
        if(serialNumber == null){
            System.out.println("No such book in system.");
            return;
        }
        boolean bookFound = false;
        for(Book b: this.books){
            if(b.getSerialNumber().equals(serialNumber)){
                bookFound = true;
                boolean rentSucess = renter.rent(b);
                if(rentSucess){
                    System.out.println("Success.");
                } else {
                    System.out.println("Book is currently unavailable.");
                }
                return;
            }
        }
        if(bookFound == false){
            System.out.println("No such book in system.");
            return;
        }
    }

    public void relinquishBook(String memberNumber, String serialNumber){
        if(this.members == null || this.members.isEmpty()){
            System.out.println("No members in system.");
            return;
        }
        if(this.books == null || this.books.isEmpty()){
            System.out.println("No books in system.");
            return;
        }
        if(memberNumber == null){
            System.out.println("No such member in system.");
            return;
        }
        Member renter = this.members.get(0);
        boolean memberFound = false;
        for(Member m: this.members){
            if(m.getMemberNumber().equals(memberNumber)){
                memberFound = true;
                renter = m;
            }
        }
        if(memberFound == false){
            System.out.println("No such member in system.");
            return;
        }
        if(serialNumber == null){
            System.out.println("No such book in system.");
            return;
        }
        boolean bookFound = false;
        for(Book b: this.books){
            if(b.getSerialNumber().equals(serialNumber)){
                bookFound = true;
                boolean relqSucess = renter.relinquish(b);
                if(relqSucess){
                    System.out.println("Success.");
                } else {
                    System.out.println("Unable to return book.");
                }
                return;
            }
        }
        if(bookFound == false){
            System.out.println("No such book in system.");
            return;
        }
    }

    public void relinquishAll(String memberNumber){
        if(this.members == null || this.members.isEmpty()){
            System.out.println("No members in system.");
            return;
        }
        if(memberNumber == null){
            System.out.println("No such member in system.");
            return;
        }
        Member renter = this.members.get(0);
        boolean memberFound = false;
        for(Member m: this.members){
            if(m.getMemberNumber().equals(memberNumber)){
                memberFound = true;
                renter = m;
            }
        }
        if(memberFound == false){
            System.out.println("No such member in system.");
            return;
        }
        renter.relinquishAll();
        System.out.println("Success.");
    }

    public void getMember(String memberNumber){
        if(this.members == null || this.members.isEmpty()){
            System.out.println("No members in system.");
            return;
        }
        if(memberNumber == null){
            System.out.println("No such member in system.");
            return;
        }
        for(Member m: this.members){
            if(m.getMemberNumber().equals(memberNumber)){
                System.out.println(m.getMemberNumber() + ": " + m.getName());
                return;
            }
        }
        System.out.println("No such member in system.");
        return;
    }

    public void getMemberBooks(String memberNumber){
        if(this.members == null || this.members.isEmpty()){
            System.out.println("No members in system.");
            return;
        }
        if(memberNumber == null){
            System.out.println("No such member in system.");
            return;
        }
        for(Member m: this.members){
            if(m.getMemberNumber().equals(memberNumber)){
                if(m.renting() == null || m.renting().isEmpty()){
                    System.out.println("Member not currently renting.");
                    return;
                }
                for(Book b: m.renting()){
                    System.out.println(b.shortString());
                }
                return;
            }
        }
        System.out.println("No such member in system.");
        return;
    }

    public void memberRentalHistory(String memberNumber){
        if(this.members == null || this.members.isEmpty()){
            System.out.println("No members in system.");
            return;
        }
        if(memberNumber == null){
            System.out.println("No such member in system.");
            return;
        }
        for(Member m: this.members){
            if(m.getMemberNumber().equals(memberNumber)){
                if(m.history() == null || m.history().isEmpty()){
                    System.out.println("No rental history for member.");
                    return;
                }
                for(Book b: m.history()){
                    System.out.println(b.shortString());
                }
                return;
            }
        }
        System.out.println("No such member in system.");
        return;
    }

    public void addMember(String name){
        if(name == null){
            return;
        }
        Integer memInt = 100000 + this.members.size();
        String memNum = memInt.toString();
        Member newMember = new Member(name, memNum);
        this.members.add(newMember);
        System.out.println("Success.");
    }

    public void saveCollection(String filename){
        if(this.books == null || this.books.isEmpty()){
            System.out.println("No books in system.");
            return;
        }
        Collection<Book> bookcollect = this.books;
        Book.saveBookCollection(filename, bookcollect);
        System.out.println("Success.");
    }

    public void addCollection(String filename){
        File f = new File(filename);
        if(f.exists() == false){
            System.out.println("No such collection.");
            return;
        }
        List<Book> booklist = Book.readBookCollection(filename);
        if(booklist == null){
            System.out.println("No books have been added to the system.");
            return;
        }
        int count = 0;
        for(Book b: booklist){
            boolean exist = false;
            for(Book existB: this.books){
                if(b.getSerialNumber().equals(existB.getSerialNumber())){
                    exist = true;
                    break;
                }
            }
            if(!exist){
                this.books.add(b);
                count += 1;
            }
        }
        if(count == 0){
            System.out.println("No books have been added to the system.");
            return;
        }
        System.out.println(count + " books successfully added.");
    }

    public void common(String[] memberNumbers){
        if(this.members == null || this.members.isEmpty()){
            System.out.println("No members in system.");
            return;
        }
        if(this.books == null || this.books.isEmpty()){
            System.out.println("No books in system.");
            return;
        }
        if(memberNumbers == null || memberNumbers.length == 0){
            System.out.println("No such member in system.");
            return;
        }
        ArrayList<Member> mlist = new ArrayList<Member>();
        for(String memNum: memberNumbers){
            boolean memberInSys = false;
            for(Member m: this.members){
                if(m.getMemberNumber().equals(memNum)){
                    memberInSys = true;
                    Member mtoAdd = m;
                    if(mlist.indexOf(m) != -1){
                        System.out.println("Duplicate members provided.");
                        return;
                    }
                    mlist.add((mtoAdd));
                }
            }
            if(memberInSys == false){
                System.out.println("No such member in system.");
                return;
            }
        }
        Member[] mArr = mlist.toArray(new Member[mlist.size()]);
        List<Book> combook = Member.commonBooks(mArr);
        if(combook == null || combook.isEmpty()){
            System.out.println("No common books.");
            return;
        }
        for(Book b: combook){
            System.out.println(b.shortString());
        }
    }

    // run method
    public void run(){
        Scanner sc = new Scanner(System.in);
        System.out.print("user: ");
        while(sc.hasNextLine()){
            String s = sc.nextLine();
            if(s.equalsIgnoreCase("EXIT")){
                break;
            }
            if(s.equalsIgnoreCase("COMMANDS")){
                System.out.println(HELP_STRING);
                System.out.println();
                System.out.print("user: ");
                continue;    
            } else if(s.equalsIgnoreCase("LIST ALL")){
                this.getAllBooks(false);
                System.out.print("user: ");  
            } else if(s.equalsIgnoreCase("LIST ALL LONG")){
                this.getAllBooks(true);
                System.out.print("user: ");  
            } else if(s.equalsIgnoreCase("LIST AVAILABLE")){
                this.getAvailableBooks(false);
                System.out.print("user: "); 
            } else if(s.equalsIgnoreCase("LIST AVAILABLE LONG")){
                this.getAvailableBooks(true);
                System.out.print("user: ");  
            } else if(s.equalsIgnoreCase("NUMBER COPIES")){
                this.getCopies();
                System.out.println();
                System.out.print("user: ");   
            } else if(s.equalsIgnoreCase("LIST GENRES")){
                this.getGenres();
                System.out.println();
                System.out.print("user: ");    
            } else if(s.equalsIgnoreCase("LIST AUTHORS")){
                this.getAuthors();
                System.out.println();
                System.out.print("user: ");    
            }
            // commands require further parsing
            String[] sArr = s.split(" ");
            if(sArr.length < 2){
                continue;
            } else if(sArr[0].equalsIgnoreCase("GENRE")){
                String g = s.substring(6);
                this.getBooksByGenre(g);
                System.out.println();
                System.out.print("user: ");
            } else if(sArr[0].equalsIgnoreCase("AUTHOR")){
                String a = s.substring(7);
                this.getBooksByAuthor(a);
                System.out.println();
                System.out.print("user: ");
            } else if(sArr[0].equalsIgnoreCase("BOOK")){
                if(sArr.length == 3 && sArr[1].equalsIgnoreCase("HISTORY")){
                    String sn = sArr[2];
                    this.bookHistory(sn);
                    System.out.println();
                    System.out.print("user: ");
                } else if(sArr.length == 3 && sArr[2].equalsIgnoreCase("LONG")){
                    this.getBook(sArr[1], true);
                    System.out.println();
                    System.out.print("user: ");
                } else if(sArr.length == 2){
                    this.getBook(sArr[1], false);
                    System.out.println();
                    System.out.print("user: ");
                }
            } else if(sArr.length == 3 && sArr[0].equalsIgnoreCase("RENT")){
                this.rentBook(sArr[1], sArr[2]);
                System.out.println();
                System.out.print("user: ");
            } else if(sArr.length == 3 && sArr[0].equalsIgnoreCase("RELINQUISH")){
                if(sArr[1].equalsIgnoreCase("ALL")){
                    this.relinquishAll(sArr[2]);
                    System.out.println();
                    System.out.print("user: ");
                } else {
                    this.relinquishBook(sArr[1], sArr[2]);
                    System.out.println();
                    System.out.print("user: ");
                }   
            } else if(sArr[0].equalsIgnoreCase("MEMBER")){
                if(sArr.length == 2){
                    this.getMember(sArr[1]);
                    System.out.println();
                    System.out.print("user: ");
                } else if(sArr.length == 3){
                    if(sArr[1].equalsIgnoreCase("BOOKS")){
                        this.getMemberBooks(sArr[2]);
                        System.out.println();
                        System.out.print("user: ");
                    } else if(sArr[1].equalsIgnoreCase("HISTORY")){
                        this.memberRentalHistory(sArr[2]);
                        System.out.println();
                        System.out.print("user: ");
                    }
                }
            } else if(sArr[0].equalsIgnoreCase("ADD")){
                if(sArr.length == 4 && sArr[1].equalsIgnoreCase("BOOK")){
                    this.addBook(sArr[2], sArr[3]);
                    System.out.println();
                    System.out.print("user: ");
                } else if(sArr.length >= 3 && sArr[1].equalsIgnoreCase("MEMBER")){
                    this.addMember(s.substring(11));
                    System.out.println();
                    System.out.print("user: ");
                } else if(sArr.length == 3 && sArr[1].equalsIgnoreCase("COLLECTION")){
                    this.addCollection(sArr[2]);
                    System.out.println();
                    System.out.print("user: ");
                }
            } else if(sArr.length == 3 && sArr[0].equalsIgnoreCase("SAVE") && sArr[1].equalsIgnoreCase("COLLECTION")){
                this.saveCollection(sArr[2]);
                System.out.println();
                System.out.print("user: ");
            } else if(sArr[0].equalsIgnoreCase("COMMON")){
                String[] mems = new String[sArr.length -1];
                int i = 0;
                while(i < mems.length){
                    mems[i] = sArr[i+1];
                    i += 1;
                }
                this.common(mems);
                System.out.println();
                System.out.print("user: ");
            }
        }
        System.out.println("Ending Library process.");
    }

    // main method
    public static void main(String[] args){
        Library myLibrary = new Library();
        myLibrary.run();
    }
}
