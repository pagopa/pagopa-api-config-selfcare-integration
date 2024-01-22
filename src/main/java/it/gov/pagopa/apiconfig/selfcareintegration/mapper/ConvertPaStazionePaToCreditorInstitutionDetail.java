package it.gov.pagopa.apiconfig.selfcareintegration.mapper;

import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CreditorInstitutionDetail;
import it.gov.pagopa.apiconfig.starter.entity.IntermediariPa;
import it.gov.pagopa.apiconfig.starter.entity.Pa;
import it.gov.pagopa.apiconfig.starter.entity.PaStazionePa;
import it.gov.pagopa.apiconfig.starter.entity.Stazioni;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class ConvertPaStazionePaToCreditorInstitutionDetail implements Converter<PaStazionePa, CreditorInstitutionDetail> {

    @Override
    public CreditorInstitutionDetail convert(MappingContext<PaStazionePa, CreditorInstitutionDetail> context) {

        PaStazionePa src = context.getSource();
        Pa pa = src.getPa();
        Stazioni stazioni = src.getFkStazione();
        IntermediariPa intermediariPa = stazioni.getIntermediarioPa();

        return CreditorInstitutionDetail.builder()
                .businessName(pa.getRagioneSociale())
                .creditorInstitutionCode(pa.getIdDominio())
                .cbillCode(pa.getCbill())
                .brokerBusinessName(intermediariPa.getCodiceIntermediario())
                .brokerCode(intermediariPa.getIdIntermediarioPa())
                .stationCode(stazioni.getIdStazione())
                .stationEnabled(stazioni.getEnabled())
                .stationVersion(stazioni.getVersione())
                .auxDigit(src.getAuxDigit())
                .segregationCode(getDoubleDigitCode(src.getSegregazione()))
                .applicationCode(getDoubleDigitCode(src.getProgressivo()))
                .broadcast(src.getBroadcast())
                .build();
    }

    private String getDoubleDigitCode(Long code) {
        String doubleDigitCode = null;
        if (code != null) {
            doubleDigitCode = String.format("%02d", code);
        }
        return doubleDigitCode;
    }
}
