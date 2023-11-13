This directory contains files with Github dependent information for every java software library analysed.
This contains more clients than what got used in the UCov case study.

------------------------------

// Below is the code used at the time to fetch dependents from github using the maracas library:

private static List<GitHubClient> getClientsList(String owner, String name) {
    System.out.println("Creating repo object");
    Repository repository = new Repository(owner, name, "", "");
    System.out.println("Creating gitHubClientsFetcher object");
    GitHubClientsFetcher gitHubClientsFetcher = new GitHubClientsScraper(Duration.ofDays(1));
    System.out.println("Creating fetchClients list");
    return gitHubClientsFetcher.fetchClients(repository, GitHubClientsFetcher.ClientFilter.ALL, Integer.MAX_VALUE);
}

public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
    List<GitHubClient> commonsCliClients = getClientsList("apache", "commons-cli");
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.writeValue(Path.of("/data/ucov/commonscliclientsgithub.json").toFile(), commonsCliClients);

    List<GitHubClient> jsoupClients = getClientsList("jhy", "jsoup");
    mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.writeValue(Path.of("/data/ucov/jsoupclientsgithub.json").toFile(), jsoupClients);

    List<GitHubClient> sparkClients = getClientsList("perwendel", "spark");
    mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.writeValue(Path.of("/data/ucov/sparkclientsgithub.json").toFile(), sparkClients);
}

------------------------------

// Below is the code used at the time to select a given amount of projects for our case study from these:

public static List<GitHubClient> getRandomElement(List<GitHubClient> list, int totalItems) {
        Random rand = new Random();
        List<GitHubClient> newList = new ArrayList<>();
        for (int i = 0; i < totalItems; i++) {
            int randomIndex = rand.nextInt(list.size());
            newList.add(list.get(randomIndex));
            list.remove(randomIndex);
        }
    return newList;
}

public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(GitHubClient[].class);
        GitHubClient[] commonsCliClients = reader.readValue(Path.of("/data/ucov/commonscliclientsgithub.json").toFile());
        GitHubClient[] jsoupClients = reader.readValue(Path.of("/data/ucov/jsoupclientsgithub.json").toFile());
        GitHubClient[] sparkClients = reader.readValue(Path.of("/data/ucov/sparkclientsgithub.json").toFile());

        List<GitHubClient> definitiveCommonsCliClients = getRandomElement(new ArrayList<>(List.of(commonsCliClients)), 372);
        List<GitHubClient> definitiveJsoupClients = getRandomElement(new ArrayList<>(List.of(jsoupClients)), 372);
        List<GitHubClient> definitiveSparkClients = getRandomElement(new ArrayList<>(List.of(sparkClients)), 373);

        String[] urlsCommonsCliClients = definitiveCommonsCliClients.stream().map(t -> "git clone --depth 1 https://github.com/" + t.owner() + "/" + t.name() + ".git /data/ucov/repro/commons-cli/Clients/" + t.owner() + "/" + t.name()).toArray(String[]::new);
        String[] urlsJsoupClients = definitiveJsoupClients.stream().map(t -> "git clone --depth 1 https://github.com/" + t.owner() + "/" + t.name() + ".git /data/ucov/repro/jsoup/Clients/" + t.owner() + "/" + t.name()).toArray(String[]::new);
        String[] urlsSparkClients = definitiveSparkClients.stream().map(t -> "git clone --depth 1 https://github.com/" + t.owner() + "/" + t.name() + ".git /data/ucov/repro/spark/Clients/" + t.owner() + "/" + t.name()).toArray(String[]::new);

        Files.write(Path.of("/data/ucov/cloneCli.sh"), List.of(urlsCommonsCliClients));
        Files.write(Path.of("/data/ucov/cloneJsoup.sh"), List.of(urlsJsoupClients));
        Files.write(Path.of("/data/ucov/cloneSpark.sh"), List.of(urlsSparkClients));
}

------------------------------