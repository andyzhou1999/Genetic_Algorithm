import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

class City{

    int x;
    int y;
    int z;

    City(){
        x = 0;
        y = 0;
        z = 0;
    }

    City(int x, int y, int z){

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String toString(){

        return x + " " + y + " " + z;
    }
}

class Pair implements Comparable{

    List<Integer> path;
    double score;

    Pair(){

    }

    Pair(List<Integer> p, double s){

        path = p;
        score = s;
    }


    @Override
    public int compareTo(Object o) {

        Pair rhs = (Pair) o;
        if (this.score < rhs.score){

            return -1;
        }
        else if (this.score > rhs.score){

            return 1;
        }
        else{

            return 0;
        }
    }

    public String toString(){

        return path.toString();
    }
}

class TSP{

    Random random = new Random(10);
    List<City> cities;
    List<Pair> population;

    TSP(){

        Scanner scan = null;
        cities = new ArrayList<>();
        try {
            scan = new Scanner(new File("input3.txt"));

            int numCity = Integer.parseInt(scan.nextLine());

            while (numCity-- > 0){

                cities.add(new City(scan.nextInt(), scan.nextInt(), scan.nextInt()));
            }

            population = initializePopulation(20);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally{

            if (scan != null){

                scan.close();
            }
        }
    }

    public void outputPath(){

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File("output.txt"));

            Pair best = population.get(0);
            for (Integer i : best.path){

                pw.println(cities.get(i));
            }

            pw.println(cities.get(best.path.get(0)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally{

            if (pw != null){

                pw.close();
            }

            System.out.println(population.get(0).score);
        }
    }

    public void approximate(){

        int generation = 10000;

        while (generation-- > 0){

            Collections.sort(population);
            //System.out.println("Current best path score: " + population.get(0).score);
            //System.out.println(population.get(0));

            List<Pair> pool = getMatingPool(1);
            population = getChildren(pool, 1);
        }

        Collections.sort(population);
        //System.out.println("Current best path score: " + population.get(0).score);
        outputPath();
    }

    public List<Pair> getChildren(List<Pair> pool, int top){

        List<Pair> children = new ArrayList<>();
        for (int i = 0; i < top; i++){

            children.add(pool.get(i));
        }

        Collections.shuffle(pool);
        for (int i = 0; i < population.size() - top; i++){

            int start = (int)(0.1 * cities.size() + random.nextDouble() * 0.2 * cities.size());
            int end = (int)(0.5 * cities.size() + random.nextDouble() * 0.2 * cities.size());
            Pair child = crossover(pool.get(i), pool.get(pool.size() - i - 1), start, end);

            mutate(child);
            children.add(child);
        }

        return children;

    }

    public void mutate(Pair child) {

        final double RATE = 0.1;

        for (int i = 0; i < cities.size(); i++){

            if (random.nextDouble() < RATE){


                int change = random.nextInt(cities.size());
                int temp = child.path.get(i);

                child.path.set(i, child.path.get(change));
                child.path.set(change, temp);
            }
        }
    }

    public List<Pair> getMatingPool(int top){

        List<Double> probability = new ArrayList<>();
        List<Pair> pool = new ArrayList<>();
        double sum = 0;

        for (int i = 0; i < top; i++){

            pool.add(population.get(i));
        }

        for (int i = 0; i < population.size(); i++){

            sum += 1 / population.get(i).score;
            probability.add(sum);
        }

        for (int i = 0; i < probability.size(); i++){

            probability.set(i, probability.get(i) / sum);
        }

        for (int i = 0; i < population.size() - top; i++){

            double pick = random.nextDouble();
            int j = 0;
            for (; j < probability.size(); j++){

                if (pick <= probability.get(j)){

                    pool.add(population.get(j));
                    break;
                }
            }
        }

        return pool;
    }

    public Pair crossover(Pair p1, Pair p2, int start, int end){

        Integer[] child = new Integer[p1.path.size()];
        Set<Integer> visited = new HashSet<>();

        for (int i = start; i <= end; i++){

            child[i] = p1.path.get(i);
            visited.add(p1.path.get(i));
        }

        for (int i = 0; i < start; i++){

            for (int j : p2.path){

                if (!visited.contains(j)){

                    child[i] = j;
                    visited.add(j);
                    break;
                }
            }
        }

        for (int i = end + 1; i < p1.path.size(); i++){

            for (int j : p2.path){

                if (!visited.contains(j)){

                    child[i] = j;
                    visited.add(j);
                    break;
                }
            }
        }

        List<Integer> _child = Arrays.asList(child);
        return new Pair(_child, getScore(_child));

    }

    public double getScore(List<Integer> path){

        double score = 0;
        int i = 1;
        for (; i < path.size(); i++){

            City prev = cities.get(path.get(i - 1));
            City curr = cities.get(path.get(i));

            score += Math.sqrt(Math.pow(prev.x - curr.x, 2) + Math.pow(prev.y - curr.y, 2) + Math.pow(prev.z - curr.z, 2));
        }

        City prev = cities.get(path.get(i - 1));
        City curr = cities.get(0);
        score += Math.sqrt(Math.pow(prev.x - curr.x, 2) + Math.pow(prev.y - curr.y, 2) + Math.pow(prev.z - curr.z, 2));
        return score;
    }

    private List<Pair> initializePopulation(int size){

        //randomly pick cities if total cities is too big
        //otherwise we can generate all permutations
        List<Pair> result = new ArrayList<>();

        for (int i = 0; i < size; i++){

            recursiveHelper(result, new ArrayList<>());
        }

        Collections.shuffle(result);
        return result;

    }

    private void recursiveHelper(List<Pair> result, List<Integer> curr){


        if (curr.size() == cities.size()){

            result.add(new Pair(curr, getScore(curr)));
        }
        else{

            int i = random.nextInt(cities.size());
            while (repeat(curr, i)){

                i = random.nextInt(cities.size());
            }


            //System.out.println("adding valid city");
            curr.add(i);
            recursiveHelper(result, new ArrayList<>(curr));
            curr.remove(curr.size() - 1);


        }
    }

    private boolean repeat(List<Integer> curr, int i) {

        for (Integer e : curr){

            if (e == i) return true;
        }

        return false;
    }


}

public class homework {

    public static void main(String[] args){

        TSP tsp= new TSP();

        tsp.approximate();

    }
}
