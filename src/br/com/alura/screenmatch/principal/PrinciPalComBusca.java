package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.exceptions.ErroDeConversaoDeAnoException;
import br.com.alura.screenmatch.modelos.Titulo;
import br.com.alura.screenmatch.modelos.TituloOmdb;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrinciPalComBusca {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner leitura = new Scanner(System.in);
        String busca = "";
        List<Titulo> ListaTitulos = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .create();

        while (!busca.equalsIgnoreCase("sair")) {

            System.out.println("Digite um filme para busca: ");
            busca = leitura.nextLine();

            if (busca.equalsIgnoreCase("sair")) {
                break;
            }

            String URL = "https://www.omdbapi.com/?t=" + busca.replace(" ", "+") + "&apikey=4eb6c41b";
            System.out.println(URL);
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(URL))
                        .build();
                HttpResponse<String> response = client
                        .send(request, HttpResponse.BodyHandlers.ofString());

                String json = response.body();
                System.out.println(json);


//        Titulo meuTitulo = gson.fromJson(json, Titulo.class);
                TituloOmdb meuTituloOmdb = gson.fromJson(json, TituloOmdb.class);
//                System.out.println(meuTituloOmdb);
//        try {
                Titulo meuTitulo = new Titulo(meuTituloOmdb);
                System.out.println("\nTitulo já convertido");
                System.out.println(meuTitulo);

//                FileWriter escrita = new FileWriter("Filmes.txt");
//                escrita.write(meuTitulo.toString());
//                escrita.close();

                ListaTitulos.add(meuTitulo);


            } catch (NumberFormatException exception) {
                System.out.println("\nAconteceu um erro: ");
                System.out.println(exception.getMessage());
            } catch (IllegalArgumentException exception) {
                System.out.println("\nAlgum erro de argumento na busca, verifique o endereço.");
            } catch (ErroDeConversaoDeAnoException exception) {
                System.out.println(exception.getMessage());
            }

        }
        System.out.println("lista de titulos: \n" + ListaTitulos);

        FileWriter escrita = new FileWriter("filmes.json");
        escrita.write(gson.toJson(ListaTitulos));
        escrita.close();
        System.out.println("\nO programa finalizou corretamente");

    }

}
