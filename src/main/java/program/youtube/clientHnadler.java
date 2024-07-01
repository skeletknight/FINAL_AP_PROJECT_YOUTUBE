package program.youtube;

import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static program.youtube.database.*;


class clientHandler implements Runnable {
    private final Socket clientSocket;

    public clientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public static void get_video(Socket socket,String name){
        try {


            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int byteread;
            FileOutputStream fileOutputStream = new FileOutputStream("D:\\final_project\\src\\main\\resources\\videos\\"+name+".mkv");

            while ((byteread = inputStream.read(buffer)) != -1){
                fileOutputStream.write(buffer,0,byteread);
            }
            fileOutputStream.close();
            inputStream.close();






        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void get_pfp(Socket socket,String name) {
        try {


            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[8192];
            int byteread;
            FileOutputStream fileOutputStream = new FileOutputStream("D:\\final_project\\src\\main\\resources\\profile_pictures\\" + name + ".jpg");

            while ((byteread = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, byteread);
            }
            fileOutputStream.close();
            inputStream.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void server_send_pfp(Socket clientSocket) throws IOException {
        FileInputStream videoFile = new FileInputStream("D:\\final_project\\src\\main\\resources\\profile_pictures\\1.jpg");

        while (true) {
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            out.writeUTF("msg");

            OutputStream outputStream = clientSocket.getOutputStream();

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = videoFile.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            videoFile.close();
            clientSocket.close();
        }
    }



    public static void server_video_sender(Socket clientSocket) throws IOException {
        FileInputStream videoFile = new FileInputStream("C:\\Users\\Sepanta\\Downloads\\@movieo_bot.Black.Bullet.E02.720p.BluRay.@movieo_bot.mkv");

        while (true) {
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            out.writeUTF("msg");

            OutputStream outputStream = clientSocket.getOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = videoFile.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            videoFile.close();
            clientSocket.close();
        }


    }





    @Override
    public void run() {
        try {
            // Get input and output streams
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Read client input
            String clientInput;

            while ((clientInput = in.readLine()) != null) {

                switch (clientInput) {
                    case "getfile" -> {
                        String name = in.readLine();
                        get_video(clientSocket, name);
                        out.println("get");
                    }
                    case "sendfile" -> {
                        server_video_sender(clientSocket);
                        out.println("sent");
                    }
                    case "sign_in" -> {
                        System.out.println("on");
                        server_sign_in(clientSocket);
                        System.out.println("got the info");
                    }
                    case "get_pfp" -> {
                        System.out.println("pfp");
                        String name = in.readLine();
                        get_pfp(clientSocket, name);
                    }
                    case "send_pfp" ->{

                        System.out.println("send_pfp");
                        // i dont remmeber what is the usee of this
                        String name = in.readLine();
                        server_send_pfp(clientSocket);
                    }
                    case "log_in" ->{
                        System.out.println("login");
                        server_log_in(clientSocket);
                    }
                    case "sign_in_name" ->{
                        System.out.println("sign_in_name");
                        sign_in_get_name(clientSocket);
                    }
                    case "sign_in_birth_dates" ->{
                        System.out.println("birth");
                        sign_in_get_birth_dates(clientSocket);
                    }
                    case "sign_in_eamil,pass,bio" ->{
                        System.out.println("email");
                        sign_in_get_email_pass_bio(clientSocket);
                    }
                }

                // Process client input (you can customize this part)
                //String response = "Hello, client!"; // Your custom response

                // Send response back to client
                //out.println(response);
            }

            // Clean up
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void server_sign_in(Socket clientSocket) {
        try {
            // Read data from the client
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String clientData = reader.readLine();
            System.out.println("Received sign-in data from client: " + clientData);

            // Parse the JSON data
            JSONObject json = new JSONObject(clientData);
            String name = json.getString("name");
            String userName = json.getString("user_name");
            String familyName = json.getString("family_name");
            String password = json.getString("user_password");
            String bio = json.getString("bio");
            int birthYear = json.getInt("birth_year");
            String birthMonth = json.getString("birth_month");
            int birthDay = json.getInt("birth_day");
            database database = new database();
            //inserting_user_info(name,userName,familyName,password,bio,birthYear,birthMonth,birthDay);

            // Insert data into the database (use your actual method here)
            // Example (assuming you have a method called insertIntoDatabase):
            // insertIntoDatabase(name, userName, familyName, password, bio, birthYear, birthMonth, birthDay);

            // Send a response back to the client
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            writer.println("Server received your sign-in data: " + clientData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sign_in_get_name(Socket clientSocket) throws IOException {
        // Read data from the client
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String clientData = reader.readLine();
            System.out.println("Received sign-in data from client: " + clientData);

            // Parse the JSON data
            JSONObject json = new JSONObject(clientData);
            String name = json.getString("name");
            String userName = json.getString("user_name");
            String familyName = json.getString("family_name");

            database database = new database();
            inserting_name_username_lname(name,familyName,userName);
            //inserting_user_info(name,userName,familyName,password,bio,birthYear,birthMonth,birthDay);

            // Insert data into the database (use your actual method here)
            // Example (assuming you have a method called insertIntoDatabase):
            // insertIntoDatabase(name, userName, familyName, password, bio, birthYear, birthMonth, birthDay);

            // Send a response back to the client
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            writer.println("Server received your sign-in data: " + clientData);
        } catch (IOException e) {
        e.printStackTrace();
        }
    }



    public static void sign_in_get_birth_dates(Socket clientSocket) throws IOException {
        // Read data from the client
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String clientData = reader.readLine();
            System.out.println("Received sign-in data from client: " + clientData);

            // Parse the JSON data
            JSONObject json = new JSONObject(clientData);
            String user_name = json.getString("user_name");
            int birth_day = json.getInt("birth_day");
            String birth_month = json.getString("birth_month");
            int birth_year = json.getInt("birth_year");
            String gender = json.getString("gender");

            database database = new database();
            inserting_birth_date_and_gender(birth_year,birth_month,birth_day,user_name,gender);
            //inserting_user_info(name,userName,familyName,password,bio,birthYear,birthMonth,birthDay);

            // Insert data into the database (use your actual method here)
            // Example (assuming you have a method called insertIntoDatabase):
            // insertIntoDatabase(name, userName, familyName, password, bio, birthYear, birthMonth, birthDay);

            // Send a response back to the client
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            writer.println("Server received your sign-in data: " + clientData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sign_in_get_email_pass_bio(Socket clientSocket){
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String clientData = reader.readLine();
            System.out.println("Received sign-in data from client: " + clientData);

            // Parse the JSON data
            JSONObject json = new JSONObject(clientData);
            String user_name = json.getString("user_name");
            String email = json.getString("email");
            String password = json.getString("password");
            String bio = json.getString("bio");


            database database = new database();
            inserting_password_email_bio(user_name,password,email,bio);
            //inserting_user_info(name,userName,familyName,password,bio,birthYear,birthMonth,birthDay);

            // Insert data into the database (use your actual method here)
            // Example (assuming you have a method called insertIntoDatabase):
            // insertIntoDatabase(name, userName, familyName, password, bio, birthYear, birthMonth, birthDay);

            // Send a response back to the client
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            writer.println("Server received your sign-in data: " + clientData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void server_log_in(Socket clientSocket){
        try {
            // Read data from the client
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String clientData = reader.readLine();
            System.out.println("Received login-in data from client: " + clientData);

            // Parse the JSON data
            JSONObject json = new JSONObject(clientData);
            String userName = json.getString("user_name");
            String password = json.getString("user_password");
            database database = new database();
            //inserting_user_info(name,userName,familyName,password,bio,birthYear,birthMonth,birthDay);
            database.login(userName,password);
            // Insert data into the database (use your actual method here)
            // Example (assuming you have a method called insertIntoDatabase):
            // insertIntoDatabase(name, userName, familyName, password, bio, birthYear, birthMonth, birthDay);

            // Send a response back to the client
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            writer.println("Server received your sign-in data: " + clientData);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}


