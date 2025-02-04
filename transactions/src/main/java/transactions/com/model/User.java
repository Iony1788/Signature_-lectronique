package transactions.com.model;


public class User {
	private String Username;
	private String firstName;
	private String password;
	private String Email;

	public User(String password, String firstName, String Username , String Email) {
		this.firstName = firstName;
		this.password = password;
		this.Username = Username;
		this.Email = Email;
	}

	// Getters and setters
	public String getUsername() {
		return Username;
	}

	public void setUsername(String Username) {
		this.Username = Username;
	}

	public String getfirstName() {
		return firstName;
	}

	public void setfirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String Email) {
		this.Email = Email;
	}
}
