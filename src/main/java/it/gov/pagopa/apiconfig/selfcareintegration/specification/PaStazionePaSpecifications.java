package it.gov.pagopa.apiconfig.selfcareintegration.specification;

import it.gov.pagopa.apiconfig.starter.entity.IntermediariPa;
import it.gov.pagopa.apiconfig.starter.entity.PaStazionePa;
import it.gov.pagopa.apiconfig.starter.entity.Stazioni;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class PaStazionePaSpecifications {


    public static Specification<PaStazionePa> filter(String paBrokerCode, Boolean enabled) {
        return (root, query, cb) -> {

            query.distinct(true);
            List<Predicate> list = new ArrayList<>();
            root.join("pa", JoinType.LEFT);
            Join<PaStazionePa, Stazioni> stazioni = root.join("fkStazione", JoinType.LEFT);
            Join<Stazioni, IntermediariPa> intermediariPa = stazioni.join("intermediarioPa", JoinType.LEFT);

            if (null != enabled) {
                list.add(cb.and(cb.equal(stazioni.get("enabled"), enabled)));
            }
            list.add(cb.and(cb.equal(intermediariPa.get("idIntermediarioPa"), paBrokerCode)));
            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
    }
}
