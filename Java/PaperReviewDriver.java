import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PaperReviewDriver {

	// DB CONNECTION INITIALIZATION
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/Assignment1";
	static final String USER = "root";
	static final String PASS = "asdfathon2";

	// -------MAIN-------
	public static void main(String[] args) {

		// INITIALIZATION OF VARIABLES
		Scanner userInput = new Scanner(System.in);
		String selection;
		Connection conn = null;
		Statement stmt = null;
		boolean run = true;

		try {
			// CONNECT TO DATABASE USING JDBC
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connecting to Paper Review database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// LOOP AS LONG AS THE USER WISHES TO CONTINUE MAKING QUERIES
			while (run) {
				// OUTPUT AVAILABLE OPTIONS TO THE USER
				System.out.println("\nWelcome to the paper review database. What is your query?");
				System.out.println("Choose a numeric option and then hit Enter.\n");
				System.out.println("    (1) - Get all data for a specific author.");
				System.out.println("    (2) - Get reviews for a Recommended paper via Paper ID Number.");
				System.out.println("    (3) - Count all sumbitted papers.");
				System.out.println("    (4) - Create a new paper submission.");
				System.out.print("\nSelection: ");
				selection = userInput.nextLine();

				// IF THE USER WANTS TO SEARCH BY AUTHOR EMAIL
				if (selection.equals("1")) {
					System.out.print("Enter the Author's email address that you wish to search by: ");
					String email = userInput.nextLine();
					RetrieveAuthor(email, conn);
				}
				// IF THE USER WANTS TO QUERY A PAPER BY ID
				else if (selection.equals("2")) {
					System.out.print("Enter the Paper ID Number that you would like to query: ");
					String paperId;
					paperId = userInput.nextLine();
					System.out.println("\nExecuting query...");

					// LAUNCH EXTERNAL METHOD
					FindReviewsOfPaper(paperId, conn);
				}
				// IF THE USER WANTS TO COUNT THE PAPERS
				else if (selection.equals("3")) {
					System.out.println("\nExecuting query...");
					stmt = conn.createStatement();

					// LAUNCH EXTERNAL METHOD
					CountPapers(conn);
				}
				// IF THE USER WANTS TO MAKE A NEW PAPER.
				else if (selection.equals("4")) {

					// FILLING OUT USER INPUT FOR THE NEW PAPER.
					System.out.print("Enter the title of the new paper: ");
					String paperTitle;
					paperTitle = userInput.nextLine();
					System.out.print("Enter the filename of this paper: ");
					String paperFilename;
					paperFilename = userInput.nextLine();
					System.out.print("Enter the filename of the abstract that belongs to this paper: ");
					String abstractFilename;
					abstractFilename = userInput.nextLine();
					System.out.print("Enter the author's email address: ");
					String email;
					email = userInput.nextLine();
					System.out.print("Enter the author's first name: ");
					String fname;
					fname = userInput.nextLine();
					System.out.print("Enter the author's last name: ");
					String lname;
					lname = userInput.nextLine();
					System.out.println("\nExecuting query...");

					// CONSTRUCTION OF THE FINAL QUERY.
					String sql;
					String sql2;
					sql = "INSERT INTO paper (Title,Abstract,Filename,Contact_author) VALUES ('" + paperTitle + "', '"
							+ abstractFilename + "', '" + paperFilename + "', '" + email + "')";

					sql2 = "INSERT INTO author (Email_id,Fname,Lname) VALUES ('" + email + "', '" + fname + "', '"
							+ lname + "')";

					// SENDING OFF THE QUERY TO THE METHOD THAT CONTACTS THE DATABASE AND INSERTS
					// THE NEWLY CREATED PAPER.
					InsertPaper(sql, sql2, conn);
				}
				System.out.print(
						"\nWould you like to continue using the database?\nInput 'Y' if yes. Otherwise, input any other key.");
				System.out.print("\nSelection: ");
				selection = userInput.nextLine();
				if (!selection.toUpperCase().equals("Y")) {
					run = false;
				}

			}
		} // HANDLE EXCEPTIONS
		catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("\nYou have been logged out.");

		// CLOSE RESOURCES
		userInput.close();
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ------METHODS------
	// METHOD THAT RETRIEVES AUTHORS BY EMAIL
	public static void RetrieveAuthor(String str, Connection conn) {
		Statement stmt = null;

		try {
			stmt = conn.createStatement();
			String fname = null;
			String lname = null;
			String find;
			find = "select * from author where Email_id = '" + str + "'";
			ResultSet rs = stmt.executeQuery(find);
			while (rs.next()) {
				String email = rs.getString("Email_id");
				fname = rs.getString("Fname");
				lname = rs.getString("Lname");
				System.out.println("\n-----AUTHOR INFORMATION-----");
				System.out.println("Email address: " + email);
				System.out.println("Full name: " + fname + " " + lname);
			}
			if (fname == null) {
				System.out.println("That author was not found in the database.");
			}
			find = "select * from paper where Contact_author = '" + fname + " " + lname + "'";
			rs = stmt.executeQuery(find);
			while (rs.next()) {
				String id = rs.getString("Id_num");
				String title = rs.getString("Title");
				String abstractFilename = rs.getString("Abstract");
				String paperFilename = rs.getString("Filename");
				System.out.println("Paper ID Number: " + id);
				System.out.println("Paper title: " + title);
				System.out.println("Paper filename: " + paperFilename);
				System.out.println("Abstract filename: " + abstractFilename);
			}
		} catch (SQLException e) {
		}
	}

	// METHOD THAT FINDS REVIEWS OF PAPERS BY ID IF THEY'VE BEEN RECOMMENDED.
	// "RECOMMENDED" IN THIS CASE MEANS THAT THE PAPER HAS SCORED HIGHER THAN 5
	// IN THE OVERALL SCALING SYSTEM.
	public static void FindReviewsOfPaper(String str, Connection conn) {
		Statement stmt = null;
		int loops = 0;
		try {
			stmt = conn.createStatement();
			String find;
			find = "select * from review where Overall_scale > 5 and Paper_id = " + str;
			ResultSet rs = stmt.executeQuery(find);

			while (rs.next()) {
				loops++;
				System.out.println("\n-----REVIEW " + loops + "-----");
				String readability = rs.getString("Readability_scale");
				String techMerit = rs.getString("Tech_merit_scale");
				String originality = rs.getString("Originality_scale");
				String relevance = rs.getString("Relevance_scale");
				String overall = rs.getString("Overall_scale");
				String paperID = rs.getString("Paper_id");
				String reviewerName = rs.getString("Reviewer_name");
				System.out.println("Paper ID: " + paperID);
				System.out.println("Readability Grade: " + readability);
				System.out.println("Technical Merit Grade: " + techMerit);
				System.out.println("Originality Grade: " + originality);
				System.out.println("Relevance Grade: " + relevance);
				System.out.println("Overall Grade: " + overall);
				System.out.println("Reviewer's Name: " + reviewerName);
			}
			if (loops == 0) {
				System.out.println("No reviews found.");
			}
		} catch (SQLException e) {
		}
	}

	// METHOD THAT COUNTS PAPERS.
	public static void CountPapers(Connection conn) {
		Statement stmt = null;

		try {
			stmt = conn.createStatement();
			String count;
			count = "select count(*) from paper";
			ResultSet rs = stmt.executeQuery(count);
			while (rs.next()) {
				String num = rs.getString("count(*)");
				System.out.println("There are " + num + " paper(s) in this database");
			}
		} catch (SQLException e) {
		}
	}

	// METHOD THAT INSERTS A NEW PAPER.
	public static void InsertPaper(String str, String str2, Connection conn) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			stmt.executeUpdate(str);
			stmt.executeUpdate(str2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("The paper has been inserted into the database.");
	}
}