package it.gov.pagopa.apiconfig.selfcareintegration.mapper;

import static it.gov.pagopa.apiconfig.selfcareintegration.util.TestUtil.getMockChannelMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.apiconfig.Application;
import it.gov.pagopa.apiconfig.selfcareintegration.model.channel.ChannelDetails;
import it.gov.pagopa.apiconfig.starter.entity.Canali;
import it.gov.pagopa.apiconfig.selfcareintegration.util.TestUtil;
import java.io.IOException;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
class ConvertCanaliToChannelDetailsTest {

  @Test
  void testSimpleEquality() throws IOException, JSONException {

    ModelMapper mapper = new ModelMapper();
    mapper.addConverter(new ConvertCanaliToChannelDetails());

    ChannelDetails channelDetailsFromFile =
        new ObjectMapper()
            .readValue(
                TestUtil.readJsonFromFile("response/channels_details_ok1.json"),
                ChannelDetails.class);
    ChannelDetails channelDetails = mapper.map(getMockChannelMapping(), ChannelDetails.class);
    String channelDetailsFromFileJSON = TestUtil.toJson(channelDetailsFromFile);
    String channelDetailsJSON = TestUtil.toJson(channelDetails);
    JSONAssert.assertEquals(channelDetailsFromFileJSON, channelDetailsJSON, JSONCompareMode.STRICT);
  }

  @Test
  void testSimpleEquality_1() throws IOException, JSONException {

    ModelMapper mapper = new ModelMapper();
    mapper.addConverter(new ConvertCanaliToChannelDetails());
    Canali channelMapping = getMockChannelMapping();
    channelMapping.setFkCanaliNodo(null);

    ChannelDetails channelDetailsFromFile =
        new ObjectMapper()
            .readValue(
                TestUtil.readJsonFromFile("response/channels_details_ok2.json"),
                ChannelDetails.class);

    ChannelDetails channelDetails = mapper.map(channelMapping, ChannelDetails.class);
    String channelDetailsFromFileJSON = TestUtil.toJson(channelDetailsFromFile);
    String channelDetailsJSON = TestUtil.toJson(channelDetails);
    JSONAssert.assertEquals(channelDetailsFromFileJSON, channelDetailsJSON, JSONCompareMode.STRICT);
  }
}
