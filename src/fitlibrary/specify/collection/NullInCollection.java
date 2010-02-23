package fitlibrary.specify.collection;

import java.util.ArrayList;
import java.util.List;

import fitlibrary.object.DomainFixtured;

@SuppressWarnings("unchecked")
public class NullInCollection implements DomainFixtured {
	public List getList() {
		ArrayList list = new ArrayList();
		list.add(null);
		list.add("fitlibrary");
		return list;
	}

}
