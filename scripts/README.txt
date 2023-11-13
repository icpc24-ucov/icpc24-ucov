The files obtained via the SUF analysis exporting are not using correct ids for clients, belows (C#) code, aims to fix this and generate a concatenated output

------------------------------

const string sufDirectory = @"/data/ucov/Spark-2.9.3.SUFs";
const string destinationSuf = @"/data/ucov/Spark-2.9.3.SUF.Clients.Only.csv";

string[] allSUFs = Directory.EnumerateFiles(sufDirectory, "*.csv", SearchOption.TopDirectoryOnly).ToArray();

foreach (string suf in allSUFs)
{
    string[] sufContent = File.ReadAllLines(suf);
    for (int i = 0; i < sufContent.Length; i++)
    {
        sufContent[i] = sufContent[i].Replace("PLACEHOLDER_CLIENT_ID", suf.Split("/").Last().Replace("______", ":").Replace(".csv", ""));
    }

    File.AppendAllLines(destinationSuf, sufContent);
}

------------------------------