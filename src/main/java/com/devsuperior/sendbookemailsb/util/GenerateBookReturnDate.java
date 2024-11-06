package com.devsuperior.sendbookemailsb.util;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class GenerateBookReturnDate {

    public static int numDaysToReturnBook = 7;

    //definindo dada formato DD-MM-YYYY
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // pega a data formatada do emprestimo do livro
    public static String getDate(Date loan_Date) {
        Calendar calendar = dateToCalendar(loan_Date);

        //adicionaremos o número de dia (7 dias) para devolução do livro
        calendar.add(Calendar.DATE, numDaysToReturnBook);

        //e agora converte para String, mas precisamos criar outro metodo
        //para converter de calender para date,
        // pois o dateFormat precisa receber um Date (atualmente está Calendar)!
        String result = dateFormat.format(calendarToDate(calendar));
        return result;
    }

    //metodo para converter de Calendar pra Date, pois é o que o dateFormat precisa
    //para que possamos retornar uma String
    private static Date calendarToDate(Calendar calendar) {
        return calendar.getTime();
    }

    //converts Date do Calendar
    private static Calendar dateToCalendar(Date loanDate) {

        //instancia o calender com getInstance
        Calendar calendar = Calendar.getInstance();
        //e converte
        calendar.setTime(loanDate);
        return calendar;
    }

}
