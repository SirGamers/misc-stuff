import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Extractor {
	
	public static int find(byte[] bytes, byte[] toFind, int start) {
		boolean found;
		for(int i = start; i < bytes.length - toFind.length; i++) {
			found = true;
			for(int j = 0; j < toFind.length; j++) {
				if(!(toFind[j] == bytes[i+j])) {
					found = false;
				}
			}
			if(found) {
				return i;
			}
		}
		return -1;
	}
	public static void main(String[] args) throws Exception{
		Object[] FILES;
		List<String> files = new ArrayList<>();
		File file = new File("vvvvvvmusic.vvv");
		FileInputStream stream = new FileInputStream(file);
		byte[] q = new byte[(int)file.length()];
		stream.read(q);
		stream.close();
		int oggStart = find(q, "OggS".getBytes(), 0), current = 0, nextNull = 0;
		String[] tmp;
		String path;
		while(current < oggStart && current != -1) {
			nextNull = find(q, new byte[] {0b00}, current);
			path = new String(Arrays.copyOfRange(q, current, nextNull));
			tmp = path.split("/");
			path = tmp[tmp.length - 1];
			System.out.println(path);
			files.add(path);
			current = find(q, "data".getBytes(), nextNull);
		}
		FILES = files.toArray();
		System.out.println("Found " + files.size() + " ogg file records");
		int startAt = -1, 
			endAt = -1, 
			musStartAt = -1, 
			musEndAt = -1, 
			currentMus = 0,
			oldStartAt;
		byte sB;
		while(true) {
			oldStartAt = startAt;
		    startAt = find(q, "OggS".getBytes(), oldStartAt + 1);
		    endAt = find(q, "OggS".getBytes(), startAt + 1) - 1;
		    if(oldStartAt >= startAt) return;
		    if(endAt == -2) endAt = q.length - 1;
		    sB = q[startAt+5];
		    if(sB == 2) musStartAt = startAt;
		    else if(sB == 4) {
		        musEndAt = endAt;
		        System.out.printf("Found entire Ogg between %d %d\n",musStartAt,musEndAt);
		        System.out.printf("Filename: %s\n", FILES[currentMus]);
		        Files.write(new File(FILES[currentMus].toString()).toPath(), Arrays.copyOfRange(q, musStartAt, musEndAt));
		        currentMus += 1;
		    }
		}
	}
}