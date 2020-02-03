package group10;


public class BuildData
{
    public boolean buildSuccess;
    public boolean testSuccess;
    public String message;
    public BuildData(boolean buildSuccess, boolean testSuccess, String message)
    {
        this.buildSuccess = buildSuccess;
        this.message = message;
        this.testSuccess = testSuccess;
    }
}