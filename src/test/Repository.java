public class Repository<T> {  
    private List<T> entities;  
    public <K extends Serializable> void save(K key, List<? super T> backups) {
    }  
}