package it.gov.pagopa.apiconfig.selfcareintegration.mapper;

import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CreditorInstitutionDetail;
import it.gov.pagopa.apiconfig.selfcareintegration.util.Utility;
import it.gov.pagopa.apiconfig.starter.entity.IntermediariPa;
import it.gov.pagopa.apiconfig.starter.entity.Pa;
import it.gov.pagopa.apiconfig.starter.entity.PaStazionePa;
import it.gov.pagopa.apiconfig.starter.entity.Stazioni;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import static it.gov.pagopa.apiconfig.selfcareintegration.util.Utility.deNull;

public class ConvertPaStazionePaToCreditorInstitutionDetail implements Converter<PaStazionePa, CreditorInstitutionDetail> {

    @Override
    public CreditorInstitutionDetail convert(MappingContext<PaStazionePa, CreditorInstitutionDetail> context) {

        PaStazionePa src = context.getSource();
        Pa pa = src.getPa();
        Stazioni stazione = src.getFkStazione();
        IntermediariPa intermediariPa = stazione.getIntermediarioPa();

        var builder = CreditorInstitutionDetail.builder()
                .businessName(pa.getRagioneSociale())
                .creditorInstitutionCode(pa.getIdDominio())
                .pspPayment(pa.getPagamentoPressoPsp())
                .cbillCode(pa.getCbill())
                .brokerBusinessName(intermediariPa.getCodiceIntermediario())
                .brokerCode(intermediariPa.getIdIntermediarioPa())
                .stationCode(stazione.getIdStazione())
                .stationEnabled(stazione.getEnabled())
                .stationVersion(stazione.getVersione())
                .auxDigit(src.getAuxDigit())
                .segregationCode(Utility.getDoubleDigitCode(src.getSegregazione()))
                .applicationCode(Utility.getDoubleDigitCode(src.getProgressivo()))
                .broadcast(src.getBroadcast());

        if (Strings.isNotBlank(stazione.getIp())) {
            String endpointRT = String.format("%s://%s:%s%s",
                    deNull(stazione.getProtocollo()).toLowerCase(),
                    deNull(stazione.getIp()),
                    deNull(stazione.getPorta()),
                    addPrefixIfAbsent("/", deNull(stazione.getServizio()))
            );
            builder.endpointRT(endpointRT);
        }

        if (Strings.isNotBlank(stazione.getRedirectIp())) {
            String endpointRedirect = String.format("%s://%s:%s%s%s",
                    deNull(stazione.getRedirectProtocollo().toLowerCase()),
                    deNull(stazione.getRedirectIp()),
                    deNull(stazione.getRedirectPorta()),
                    addPrefixIfAbsent("/", deNull(stazione.getRedirectPath())),
                    addPrefixIfAbsent("?", deNull(stazione.getRedirectQueryString()))
            );
            builder.endpointRedirect(endpointRedirect);
        }

        if (Strings.isNotBlank(stazione.getIp4Mod())) {
        String endpointMU = String.format("%s://%s:%s%s",
                deNull(stazione.getProtocollo4Mod()).toLowerCase(),
                deNull(stazione.getIp4Mod()),
                deNull(stazione.getPorta4Mod()),
                addPrefixIfAbsent("/", deNull(stazione.getServizio4Mod()))
        );
            builder.endpointMU(endpointMU);
        }

        builder.primitiveVersion(stazione.getVersionePrimitive());
        builder.ciStatus(deNull(src.getPa().getEnabled()));
        builder.restEndpoint(stazione.getRestEndpoint());
        builder.isPaymentOptionsEnabled(stazione.getIsPaymentOptionsEnabled());
        return builder.build();
    }

    private String addPrefixIfAbsent(String prefix, String s) {
        if (!s.startsWith(prefix)) {
            return prefix + s;
        }
        return s;
    }
}
