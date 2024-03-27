package it.gov.pagopa.apiconfig.selfcareintegration.specification;

import it.gov.pagopa.apiconfig.starter.entity.Pa;
import it.gov.pagopa.apiconfig.starter.entity.PaStazionePa;
import it.gov.pagopa.apiconfig.starter.entity.Stazioni;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Specification class that define queries on {@link Stazioni} table
 */
public class StationSpecifications {

    public static Specification<Stazioni> filter(Long fkBroker, String stationId, String ciTaxCode) {
        return (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();

            query.distinct(true);
            query.orderBy(cb.asc(root.get("idStazione")));

            if (ciTaxCode != null) {
                Join<Stazioni, PaStazionePa> paStazionePaJoin = root.join("paStazionePaList", JoinType.LEFT);
                Join<PaStazionePa, Pa> paJoin = paStazionePaJoin.join("pa", JoinType.LEFT);
                list.add(cb.and(cb.equal(paJoin.get("idDominio"), ciTaxCode)));
            }

            if (stationId != null) {
                Expression<String> pattern = cb.upper(cb.literal("%" + stationId + "%"));
                list.add(cb.and(cb.like(cb.upper(root.get("idStazione")), pattern)));
            }
            list.add(cb.and(cb.equal(root.get("fkIntermediarioPa"), fkBroker)));

            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
    }

    private StationSpecifications() {}
}
