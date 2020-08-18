package cz.tefek.test.plutodb;

import java.io.File;

import cz.tefek.plutodb.LMDBEnvironment;
import cz.tefek.plutodb.LMDBSchema;
import cz.tefek.plutodb.LMDBStringKey;

public class LMDBTest
{
    public static void main(String[] args) throws Exception
    {
        var userSchema = new LMDBSchema<LMDBStringKey, UserData>("users", UserData.class);

        try (var env = new LMDBEnvironment(new File("testdb"), 80, 1024 * 1024 * 256))
        {
            try (var txn = env.createTransaction())
            {
                var db = txn.getDatabase(userSchema);

                var data = new UserData();
                data.money = -789;
                data.keks = 123456;
                data.text = "Hello world!";
                data.keys = 0x12C0FFEE;

                db.put(LMDBStringKey.from("pepega"), data);

                txn.commit();
            }

            try (var txn = env.createTransaction(true))
            {
                var db = txn.getDatabase(userSchema);

                var data = db.get(LMDBStringKey.from("pepega"));

                System.out.println(data);
            }
        }

    }
}
