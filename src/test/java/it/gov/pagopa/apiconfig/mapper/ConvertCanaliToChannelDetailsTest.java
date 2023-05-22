package it.gov.pagopa.apiconfig.mapper;

import static it.gov.pagopa.apiconfig.util.TestUtil.getMockChannel;
import static it.gov.pagopa.apiconfig.util.TestUtil.getMockChannelMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.apiconfig.Application;
import it.gov.pagopa.apiconfig.selfcareintegration.mapper.ConvertCanaliToChannelDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.channel.ChannelDetails;
import it.gov.pagopa.apiconfig.starter.entity.Canali;
import it.gov.pagopa.apiconfig.util.TestUtil;
import java.io.IOException;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class ConvertCanaliToChannelDetailsTest {

  @Test
  void testSimpleEquality() throws IOException, JSONException {

    ModelMapper mapper = new ModelMapper();
    mapper.addConverter(new ConvertCanaliToChannelDetails());
    Canali x = getMockChannelMapping();

    ChannelDetails y =
        new ObjectMapper()
            .readValue(
                TestUtil.readJsonFromFile("response/channels_details_ok1.json"),
                ChannelDetails.class);

    ChannelDetails z = mapper.map(x, ChannelDetails.class);
    String ay = TestUtil.toJson(y);
    String az = TestUtil.toJson(z);
    JSONAssert.assertEquals(ay, az, JSONCompareMode.STRICT);
  }

  @Test
  void testSimpleEquality_1() throws IOException, JSONException {

    ModelMapper mapper = new ModelMapper();
    mapper.addConverter(new ConvertCanaliToChannelDetails());
    Canali x = getMockChannelMapping();
    x.setFkCanaliNodo(null);

    ChannelDetails y =
        new ObjectMapper()
            .readValue(
                TestUtil.readJsonFromFile("response/channels_details_ok2.json"),
                ChannelDetails.class);

    ChannelDetails z = mapper.map(x, ChannelDetails.class);
    String ay = TestUtil.toJson(y);
    String az = TestUtil.toJson(z);
    JSONAssert.assertEquals(ay, az, JSONCompareMode.STRICT);
  }
}
