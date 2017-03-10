package BP.Sys.Frm;

import BP.WF.NodeFormType;

public enum PopValWorkModel {
	 /// <summary>
    /// 自定义URL
    /// </summary>
    SelfUrl,
    /// <summary>
    /// 表格模式
    /// </summary>
    TableOnly,
    /// <summary>
    /// 表格分页模式
    /// </summary>
    TablePage,
    /// <summary>
    /// 分组模式
    /// </summary>
    Group,
    /// <summary>
    /// 树展现模式
    /// </summary>
    Tree,
    /// <summary>
    /// 双实体树
    /// </summary>
    TreeDouble;
    
    private int intValue;
	private static java.util.HashMap<Integer, PopValWorkModel> mappings;
    
    public int getValue()
	{
		return this.ordinal();
	}
	
	public static PopValWorkModel forValue(int value)
	{
		return values()[value];
	}
}
