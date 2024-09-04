package it.gov.pagopa.apiconfig.selfcareintegration.mapper;

import it.gov.pagopa.apiconfig.selfcareintegration.model.station.BrokerDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.Protocol;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.StationDetails;
import it.gov.pagopa.apiconfig.starter.entity.Stazioni;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import javax.validation.Valid;

import static it.gov.pagopa.apiconfig.selfcareintegration.util.Utility.*;

public class ConvertStazioniToStationDetails implements Converter<Stazioni, StationDetails> {

    @Override
    public StationDetails convert(MappingContext<Stazioni, StationDetails> context) {
        @Valid Stazioni source = context.getSource();
        return StationDetails.builder()
                .idStazione(source.getIdStazione())
                .enabled(source.getEnabled())
                .versione(source.getVersione())
                .password(source.getPassword())
                .protocollo(Protocol.fromValue(source.getProtocollo()))
                .ip(source.getIp())
                .porta(source.getPorta())
                .servizio(source.getServizio())
                .servizioPof(source.getServizioPof())
                .protocollo4Mod(
                        source.getProtocollo4Mod() != null
                                ? Protocol.fromValue(source.getProtocollo4Mod())
                                : null)
                .intermediarioPa(source.getIntermediarioPa() != null ?
                        BrokerDetails.builder()
                                .codiceIntermediario(source.getIntermediarioPa().getCodiceIntermediario())
                                .idIntermediarioPa(source.getIntermediarioPa().getIdIntermediarioPa())
                                .faultBeanEsteso(source.getIntermediarioPa().getFaultBeanEsteso())
                                .enabled(source.getIntermediarioPa().getEnabled())
                                .build()
                        : null)
                .ip4Mod(source.getIp4Mod())
                .porta4Mod(source.getPorta4Mod())
                .servizio4Mod(source.getServizio4Mod())
                .redirectProtocollo(
                        source.getRedirectProtocollo() != null
                                ? Protocol.fromValue(source.getRedirectProtocollo())
                                : null)
                .redirectIp(source.getRedirectIp())
                .redirectPorta(source.getRedirectPorta())
                .redirectPath(source.getRedirectPath())
                .redirectQueryString(source.getRedirectQueryString())
                .proxyEnabled(source.getProxyEnabled())
                .proxyHost(source.getProxyHost())
                .proxyPort(source.getProxyPort())
                .proxyUsername(source.getProxyUsername())
                .proxyPassword(source.getProxyPassword())
                .targetHost(source.getTargetHost())
                .targetPort(source.getTargetPort())
                .targetPath(source.getTargetPath())
                .targetHostPof(source.getTargetHostPof())
                .targetPortPof(source.getTargetPortPof())
                .targetPathPof(source.getTargetPathPof())
                .flagOnline(source.getFlagOnline())
                .numThread(source.getNumThread())
                .timeoutA(source.getTimeoutA())
                .timeoutB(source.getTimeoutB())
                .timeoutC(source.getTimeoutC())
                .invioRtIstantaneo(source.getInvioRtIstantaneo())
                .versionePrimitive(source.getVersionePrimitive())
                .isConnectionSync(isConnectionSync(source))
                .createDate(toOffsetDateTime(source.getDataCreazione()))
                .build();
    }

}
