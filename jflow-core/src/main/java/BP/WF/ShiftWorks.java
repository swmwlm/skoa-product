package BP.WF;

import java.util.*;
import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Port.*;

/** 
 移交记录s 
*/
public class ShiftWorks extends Entities
{
		///#region 构造
	/** 
	 移交记录s
	*/
	public ShiftWorks()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new ShiftWork();
	}
		///#endregion

		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<ShiftWork> ToJavaList()
	{
		return (List<ShiftWork>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<ShiftWork> Tolist()
	{
		ArrayList<ShiftWork> list = new ArrayList<ShiftWork>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((ShiftWork)this.get(i));
		}
		return list;
	}
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}