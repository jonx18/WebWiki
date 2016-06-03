package wikiAnalicis.util;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentityGenerator;

import wikiAnalicis.entity.Identificable;

public class UseIdOrGenerate extends IdentityGenerator {
	@Override
    public Serializable generate(SessionImplementor session, Object obj) throws HibernateException {
        if (obj == null) throw new HibernateException(new NullPointerException()) ;

        if ((((Identificable) obj).getId()) == null) {//id is null it means generate ID
            Serializable id = super.generate(session, obj) ;
            return id;
        } else {
            return ((Identificable) obj).getId();//id is not null so using assigned id.

        }
    }
}
