package BP.WF.Template;

import java.util.ArrayList;
import java.util.List;

import BP.En.EntitiesMultiTree;
import BP.En.Entity;

/** 
 独立表单树
*/
public class FlowFormTrees extends EntitiesMultiTree
{
	/** 
	 独立表单树s
	*/
	public FlowFormTrees()
	{
	}
	/** 
	 独立表单树
	*/
	public FlowFormTrees(String flowNo)
	{
	   int i = this.Retrieve(FlowFormTreeAttr.FK_Flow, flowNo);
	   if (i == 0)
	   {
		   FlowFormTree tree = new FlowFormTree();
		   tree.setNo("100");
		   tree.setFK_Flow(flowNo);
		   tree.setName("根目录");
		   tree.setIsDir(false);
		   tree.setParentNo("0");
		   tree.Insert();

		   //创建一个节点.
		   tree.DoCreateSubNode();
	   }
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FlowFormTree();
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FlowFormTree> ToJavaList()
	{
		return (List<FlowFormTree>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FlowFormTree> Tolist()
	{
		ArrayList<FlowFormTree> list = new ArrayList<FlowFormTree>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FlowFormTree)this.get(i));
		}
		return list;
	}
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}