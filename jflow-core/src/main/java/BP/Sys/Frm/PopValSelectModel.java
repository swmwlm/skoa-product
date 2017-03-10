package BP.Sys.Frm;

public enum PopValSelectModel {
	/**
	 * 单选
	 */
    One,
    /**
     *多远
     */
    More;
    private int intValue;
    
    public int getValue()
	{
		return this.ordinal();
	}
	
	public static PopValSelectModel forValue(int value)
	{
		return values()[value];
	}
}
