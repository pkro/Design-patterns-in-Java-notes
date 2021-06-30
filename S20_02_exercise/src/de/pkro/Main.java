package de.pkro;

public class Main {

    public static void main(String[] args) throws Exception{
        Account a = new Account(new NullLog());
        for (int i = 0; i < 100; ++i)
            a.someOperation();
    }
}

interface Log
{
    // max # of elements in the log
    int getRecordLimit();

    // number of elements already in the log
    int getRecordCount();

    // expected to increment record count
    void logInfo(String message);
}

class Account
{
    private Log log;

    public Account(Log log)
    {
        this.log = log;
    }

    public void someOperation() throws Exception
    {
        int c = log.getRecordCount();
        log.logInfo("Performing an operation");
        if (c+1 != log.getRecordCount())
            throw new Exception();
        if (log.getRecordCount() >= log.getRecordLimit())
            throw new Exception();
    }
}

class NullLog implements Log
{
    private int records = 0;
    @Override
    public int getRecordLimit() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getRecordCount() {
        return records;
    }

    @Override
    public void logInfo(String message) {
        records++;
    }
}
