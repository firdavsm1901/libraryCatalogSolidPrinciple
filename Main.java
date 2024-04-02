import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


interface Identifiable {
    String getTitle();
}

interface Categorizable {
    String getType();
}

interface UserManager {
    String getName();
    String getUserId();
}

interface ItemManager {
    void addItem(Identifiable item);
    void removeItem(String itemId);
    boolean isItemLent(Identifiable item);
    Identifiable findItemByTitle(String title);
}
interface UserManagement {
    void addUser(UserManager user);
    void removeUser(String userId);
    UserManager findUserById(String userId);
}

interface Lendable {
    void lendItem(Identifiable item, UserManager user);
    void returnItem(Identifiable item);
}
class Book implements Identifiable, Categorizable {
    private String title;
    private String author;
    private String type = "Book";

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }
}

class Magazine implements Identifiable, Categorizable {
    private String title;
    private String publisher;
    private String type = "Magazine";

    public Magazine(String title, String publisher) {
        this.title = title;
        this.publisher = publisher;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }
}
class CD implements Identifiable, Categorizable {
    private String title;
    private String artist;
    private String type = "CD";

    public CD(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }
}

class LibraryUserImpl implements UserManager {
    private String name;
    private String userId;

    public LibraryUserImpl(String name, String userId) {
        this.name = name;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }
}

class LibraryLibrarian implements ItemManager, UserManagement, Lendable {
    private List<Identifiable> catalog = new ArrayList<>();
    private List<Identifiable> lentItems = new ArrayList<>();
    private List<UserManager> users = new ArrayList<>();

    @Override
    public void addItem(Identifiable item) {
        catalog.add(item);
    }

    @Override
    public void removeItem(String itemId) {
        catalog.removeIf(item -> item.getTitle().equals(itemId));
        lentItems.removeIf(item -> item.getTitle().equals(itemId)); // Remove from lent items if present
    }

    @Override
    public void addUser(UserManager user) {
        users.add(user);
    }

    @Override
    public void removeUser(String userId) {
        users.removeIf(user -> user.getUserId().equals(userId));
    }

    @Override
    public void lendItem(Identifiable item, UserManager user) {
        if (isItemLent(item)) {

            System.out.println("╠═════════════════════════════════════════╣");
            System.out.println("║           " +item.getTitle()+ " is already sent.          ║");
            System.out.println("╚═════════════════════════════════════════╝");
        } else {
            lentItems.add(item);
            System.out.println("╠═════════════════════════════════════════╣");
            System.out.println("║          " +item.getTitle()+ " lent successfully.         ║");
            System.out.println("╚═════════════════════════════════════════╝");

        }
    }

    @Override
    public void returnItem(Identifiable item) {
        if (isItemLent(item)) {
            lentItems.remove(item);
            catalog.add(item);
        }
    }

    @Override
    public UserManager findUserById(String userId) {
        for (UserManager user : users) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public Identifiable findItemByTitle(String title) {
        for (Identifiable item : catalog) {
            if (item.getTitle().equalsIgnoreCase(title)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public boolean isItemLent(Identifiable item) {
        return lentItems.contains(item);
    }
}

interface UserInteractionHandler {
    void handleUserInteraction();
}

class LibraryCommandLineInterface implements UserInteractionHandler {
    private LibraryLibrarian librarian;
    private Scanner scanner;

    public LibraryCommandLineInterface(LibraryLibrarian librarian) {
        this.librarian = librarian;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void handleUserInteraction() {
        boolean running = true;
        while (running) {
            System.out.println("╔═════════════════════════════════════════╗");
            System.out.println("║        Welcome to the Library           ║");
            System.out.println("╚═════════════════════════════════════════╝");
            System.out.println("Choose an option:");
            System.out.println("1. Add item to catalog");
            System.out.println("2. Remove item from catalog");
            System.out.println("3. Add user");
            System.out.println("4. Remove user");
            System.out.println("5. Lend item to user");
            System.out.println("6. Return item");
            System.out.println("7. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addItemToCatalog();
                    break;
                case 2:
                    removeItemFromCatalog();
                    break;
                case 3:
                    addUser();
                    break;
                case 4:
                    removeUser();
                    break;
                case 5:
                    lendItemToUser();
                    break;
                case 6:
                    returnItem();
                    break;
                case 7:
                    running = false;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private void addItemToCatalog() {
        System.out.println("╔═════════════════════════════════════════╗");
        System.out.println("║          Adding Item to Catalog         ║");
        System.out.println("╚═════════════════════════════════════════╝");
        System.out.println("Choose item type to add:");
        System.out.println("1. Book");
        System.out.println("2. Magazine");
        System.out.println("3. CD");

        int itemTypeChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (itemTypeChoice) {
            case 1:
                addBookToCatalog();
                break;
            case 2:
                addMagazineToCatalog();
                break;
            case 3:
                addCDToCatalog();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void addBookToCatalog() {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║               Adding Book                  ║");
        System.out.println("╠════════════════════════════════════════════╣");
        System.out.print("║  Enter book title: ");
        String bookTitle = scanner.nextLine();
        System.out.print("║  Enter book author: ");
        String bookAuthor = scanner.nextLine();
        Identifiable newBook = new Book(bookTitle, bookAuthor);
        librarian.addItem(newBook);
        System.out.println("╠════════════════════════════════════════════╣");
        System.out.println("║          Book added to catalog.            ║");
        System.out.println("╚════════════════════════════════════════════╝");
    }

    private void addMagazineToCatalog() {
        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║            Adding Magazine                ║");
        System.out.println("╠═══════════════════════════════════════════╣");
        System.out.print("║  Enter magazine title: ");
        String magazineTitle = scanner.nextLine();
        System.out.print("║  Enter magazine publisher: ");
        String magazinePublisher = scanner.nextLine();
        Identifiable newMagazine = new Magazine(magazineTitle, magazinePublisher);
        librarian.addItem(newMagazine);
        System.out.println("╠═══════════════════════════════════════════╣");
        System.out.println("║         Magazine added to catalog.        ║");
        System.out.println("╚═══════════════════════════════════════════╝");
    }

    private void addCDToCatalog() {
        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║               Adding CD                    ║");
        System.out.println("╠═══════════════════════════════════════════╣");
        System.out.print("║  Enter CD title: ");
        String cdTitle = scanner.nextLine();
        System.out.print("║  Enter CD artist: ");
        String cdArtist = scanner.nextLine();
        Identifiable newCD = new CD(cdTitle, cdArtist);
        librarian.addItem(newCD);
        System.out.println("╠═══════════════════════════════════════════╣");
        System.out.println("║             CD added to catalog.           ║");
        System.out.println("╚═══════════════════════════════════════════╝");
    }


    private void removeItemFromCatalog() {
        System.out.print("Enter item title to remove: ");
        String removeItemId = scanner.nextLine();
        librarian.removeItem(removeItemId);
        System.out.println("Item removed from catalog.");
    }

    private void addUser() {
        System.out.println("╔═════════════════════════════════════════╗");
        System.out.println("║               Adding User               ║");
        System.out.println("╠═════════════════════════════════════════╣");
        System.out.print("║  Enter user name: ");
        String userName = scanner.nextLine();
        System.out.print("║  Enter user ID: ");
        String userId = scanner.nextLine();
        UserManager newUser = new LibraryUserImpl(userName, userId);
        librarian.addUser(newUser);
        System.out.println("╠═════════════════════════════════════════╣");
        System.out.println("║               User added.               ║");
        System.out.println("╚═════════════════════════════════════════╝");
    }


    private void removeUser() {
        System.out.print("Enter user ID to remove: ");
        String removeUserId = scanner.nextLine();
        librarian.removeUser(removeUserId);
        System.out.println("User removed.");
    }

    private void lendItemToUser() {
        System.out.println("╔═════════════════════════════════════════╗");
        System.out.println("║           Lending item to user          ║");
        System.out.println("╠═════════════════════════════════════════╣");
        System.out.println("║  Choose item type to lend:              ║");
        System.out.println("║  1. Book                                ║");
        System.out.println("║  2. Magazine                            ║");
        System.out.println("║  3. CD                                  ║");
        System.out.println("╚═════════════════════════════════════════╝");

        int itemTypeChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (itemTypeChoice) {
            case 1:
                lendBookToUser();
                break;
            case 2:
                lendMagazineToUser();
                break;
            case 3:
                lendCDToUser();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }


    private void lendBookToUser() {
        System.out.println("╔═════════════════════════════════════════╗");
        System.out.println("║           Lending Book to User          ║");
        System.out.println("╠═════════════════════════════════════════╣");
        System.out.print("║  Enter book title to lend: ");
        String lendItemId = scanner.nextLine();
        Identifiable item = librarian.findItemByTitle(lendItemId);
        if (item == null) {
            System.out.println("╠═════════════════════════════════════════╣");
            System.out.println("║            Book not found.              ║");
            System.out.println("╚═════════════════════════════════════════╝");
            return;
        }

        System.out.print("║  Enter user ID to lend to: ");
        String lendUserId = scanner.nextLine();
        UserManager user = librarian.findUserById(lendUserId);
        if (user == null) {
            System.out.println("╠═════════════════════════════════════════╣");
            System.out.println("║             User not found.             ║");
            System.out.println("╚═════════════════════════════════════════╝");
            return;
        }

        librarian.lendItem(item, user);


    }



    private void lendMagazineToUser() {
        System.out.println("╔═════════════════════════════════════════╗");
        System.out.println("║        Lending Magazine to User         ║");
        System.out.println("╠═════════════════════════════════════════╣");
        System.out.print("║  Enter magazine title to lend: ");
        String lendItemId = scanner.nextLine();
        Identifiable item = librarian.findItemByTitle(lendItemId);
        if (item == null) {
            System.out.println("╠═════════════════════════════════════════╣");
            System.out.println("║          Magazine not found.            ║");
            System.out.println("╚═════════════════════════════════════════╝");
            return;
        }

        System.out.print("║  Enter user ID to lend to: ");
        String lendUserId = scanner.nextLine();
        UserManager user = librarian.findUserById(lendUserId);
        if (user == null) {
            System.out.println("╠═════════════════════════════════════════╣");
            System.out.println("║           User not found.               ║");
            System.out.println("╚═════════════════════════════════════════╝");
            return;
        }

        librarian.lendItem(item, user);


    }



    private void lendCDToUser() {
        System.out.println("╔═════════════════════════════════════════╗");
        System.out.println("║            Lending CD to User           ║");
        System.out.println("╠═════════════════════════════════════════╣");
        System.out.print("║  Enter CD title to lend: ");
        String lendItemId = scanner.nextLine();
        Identifiable item = librarian.findItemByTitle(lendItemId);
        if (item == null) {
            System.out.println("╠═════════════════════════════════════════╣");
            System.out.println("║              CD not found.              ║");
            System.out.println("╚═════════════════════════════════════════╝");
            return;
        }

        System.out.print("║  Enter user ID to lend to: ");
        String lendUserId = scanner.nextLine();
        UserManager user = librarian.findUserById(lendUserId);
        if (user == null) {
            System.out.println("╠═════════════════════════════════════════╣");
            System.out.println("║             User not found.             ║");
            System.out.println("╚═════════════════════════════════════════╝");
            return;
        }

        librarian.lendItem(item, user);


    }



    private void returnItem() {
        System.out.println("╔═════════════════════════════════════════╗");
        System.out.println("║                 Return                  ║");
        System.out.println("╠═════════════════════════════════════════╣");
        System.out.print("║  Enter item title to return: ");
        String returnItemId = scanner.nextLine();
        Identifiable item = librarian.findItemByTitle(returnItemId);

        if (librarian.isItemLent(item)) {
            librarian.returnItem(item);
            System.out.println("╠═════════════════════════════════════════╣");
            System.out.println("║        Item returned successfully       ║");
            System.out.println("╚═════════════════════════════════════════╝");
        } else {
            System.out.println("╠═════════════════════════════════════════╣");
            System.out.println("║             Item is not lent            ║");
            System.out.println("╚═════════════════════════════════════════╝");
        }
    }

}

// Main class
public class Main {
    public static void main(String[] args) {
        LibraryLibrarian librarian = new LibraryLibrarian();
        UserInteractionHandler interactionHandler = new LibraryCommandLineInterface(librarian);
        interactionHandler.handleUserInteraction();
    }
}