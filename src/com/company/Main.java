package com.company;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static void think() throws InterruptedException {
        //Эта функция просто выводит с новой строки три точки с паузой в 300 миллисекунд.
        for(int i = 0; i < 5; i++) {
            System.out.print(". ");
            Thread.sleep(250);
        }
        System.out.println();
    }

    private static boolean checkPassword(String password) throws IOException {
        //В этой функции мы проверяем введенную строку на соответствие требованиям к паролю
        String pattern = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}";  //Паттерн для пароля
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(password);
        return m.matches();

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        //Имя файла для сохранения/загрузки пароля нужно задать первым аргументом при запуске программы
        String magicStorage = args[0];
        // Все потоки проинициализируем непосредственно перед записью и чтением
        InputStream inputStream;
        OutputStream outputStream;
        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;
        String answer;
        String pass = "";

        /**
         * Можно обойтись без следующей boolean переменной и просто все запихнуть в switch, но мне кажется,
         * что так лучше читается.
         * Т.е. в первой половине мы только спрашиваем загружать пароль или нет, а уже потом, в зависимости от выбора,
         * загружаем, вводим и сохраняем.
         */

        boolean needOldPassword;
        System.out.println("Hey, bratishka! How was your sleep? Would you like me to bring you an old password?(y/n)");


        while(true)
        {
            answer = reader.readLine().toLowerCase();
            switch(answer) {
                case "y":
                    System.out.println("Okay, i'll bring you one!..");
                    needOldPassword = true;
                    break;
                case "n":
                    System.out.println("Okay! Then enter it by yourself...");
                    needOldPassword = false;
                    break;
                default:
                    System.out.println("We are all people... y/n?");
                    continue;
            }
            break;

        }
        /**
         * Далее: если нужно загрузить старый пароль, то пытаемся его загрузить, проверяем его на соответствие
         * требованиям и либо говорим, что сохраненный пароль неверен и следуем дальше к вводу нового пароля,
         * либо говорим, что все хорошо и выводим пароль на экран. Если файл пуст, то ловим исключение и переходим
         * к вводу нового пароля.
         **/

        if(needOldPassword)
        {
            try {
                    inputStream = new FileInputStream(magicStorage);
                    objectInputStream = new ObjectInputStream(inputStream);
                    pass = (String) objectInputStream.readObject();
                    if (!checkPassword(pass)) {

                    think();
                    System.out.println("I'm sorry, pal! There is no proper password in my magic storage, let's make a new one!");
                    needOldPassword = false;
                    } else {

                    think();
                    System.out.println("That's your old password, buddy: " + pass);
                    }

                }

            catch (EOFException e) {

                think();
                System.out.println("I'm sorry, pal! There is no proper password in my magic storage(" + pass + "), let's make a new one!");
                needOldPassword = false;}


        }

        /**
         * Дальше идет блок ввода и проверки пароля. Если пароль соответствует требованиям, то спрашиваем, сохранить
         * ли его в storage, откуда при следующем запуске его можно будет загрузить. Цикл продолжается, пока не будет
         * введен пароль соответствующий паттерну (минимум одна заглавная буква, минимум одна строчная, минимум одна цифра
         * 8-16 знаков);
         * внутри два цикла: в первом вводим пароль, а во втором спрашиваем сохранить ли новый пароль
         */

        if(!needOldPassword)
        {
            think();
            System.out.println("Enter password that matches this requirements:\n8-16 symbols, at least one upper case letter, at least one lower case letter, at least one digit\nTry it...");
            while(true)
            {
                pass = reader.readLine();
                if(checkPassword(pass))
                {
                    System.out.println("Incredible! You passed all requirements! Look at big brain!");
                    think();
                    System.out.println("Do you want me to save it for you? (y/n)");
                    while(true)
                    {
                        answer = reader.readLine().toLowerCase();
                        switch(answer){
                            case "y":
                                System.out.println("Okay, I'll store it in my magic storage! You'll see!");
                                //Сериализуем введенный пароль:
                                outputStream = new FileOutputStream(magicStorage);
                                objectOutputStream = new ObjectOutputStream(outputStream);
                                objectOutputStream.writeObject(pass);
                                objectOutputStream.close();
                                outputStream.close();
                                break;
                            case "n":
                                System.out.println("Okay! As you wish! Then we finish with this.");
                                break;
                            default:
                                System.out.println("Try harder!.. y/n?");
                                continue;
                        }
                        break;

                    }
                    break;
                }
                else
                {
                    System.out.println("I'm sorry, but your password doesn't match it... Try again!");
                }
            }

        }
        think();
        reader.close();
        System.out.println("Au Revoir! That's it...");

    }
}
