/*
 * #%L
 * Wisdom-Framework
 * %%
 * Copyright (C) 2013 - 2014 Wisdom Framework
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.wisdom.i18n;

import org.junit.Test;
import org.wisdom.api.http.Status;
import org.wisdom.test.http.HttpResponse;
import org.wisdom.test.parents.WisdomBlackBoxTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Check the behavior of the internationalization controller.
 */
public class I18NControllerIT extends WisdomBlackBoxTest {

    /**
     * Deploy the test bundle as we need the messages.
     */
    @Override
    public boolean deployTestBundle() {
        return true;
    }

    @Test
    public void testThatWeCanRetrieveBundles() throws Exception {
        String bundle = get("/i18n/bundles/Messages_fr.properties").asString().body();
        assertThat(bundle)
                .contains("welcome=bonjour")
                .contains("lang=français");

        // No Locale
        bundle = get("/i18n/bundles/Messages.properties").asString().body();
        assertThat(bundle)
                .contains("welcome=hello")
                .contains("lang=english");

        // Not provided locale
        HttpResponse<String> response = get("/i18n/bundles/.properties").asString();
        assertThat(response.code()).isEqualTo(Status.NOT_FOUND);


        // Not provided locale
        response = get("/i18n/bundles/.properties").asString();
        assertThat(response.code()).isEqualTo(Status.NOT_FOUND);
    }


    @Test
    public void mimicJQueryI18nPlugin() throws Exception {
        HttpResponse<String> response = get("/i18n/bundles/Messages.properties").asString();
        assertThat(response.code()).isEqualTo(Status.OK);
        assertThat(response.body())
                .contains("welcome=hello")
                .contains("extra=extra");

        response = get("/i18n/bundles/Messages_fr.properties").asString();
        assertThat(response.code()).isEqualTo(Status.OK);
        assertThat(response.body())
                .contains("welcome=bonjour")
                .doesNotContain("extra");

        response = get("/i18n/bundles/Messages_fr_FR.properties").asString();
        System.out.println(response.body());
        assertThat(response.code()).isEqualTo(Status.NOT_FOUND);
    }

    @Test
    public void testThatWeCanRetrieveMessagesIndividuallyUsingHttpHeader() throws Exception {
        HttpResponse<String> response = get("/i18n/welcome").header("Accept-Language", "en").asString();
        assertThat(response.body()).isEqualTo("hello");

        response = get("/i18n/welcome").header("Accept-Language", "fr").asString();
        assertThat(response.body()).isEqualTo("bonjour");

        // Not supported locale, delegate to default
        response = get("/i18n/welcome").header("Accept-Language", "fr-FR").asString();
        assertThat(response.body()).isEqualTo("hello");
        response = get("/i18n/welcome").header("Accept-Language", "fr_FR").asString();
        assertThat(response.body()).isEqualTo("hello");

        response = get("/i18n/welcome").header("Accept-Language", "fr,en;q=0.5").asString();
        assertThat(response.body()).isEqualTo("bonjour");

        response = get("/i18n/welcome").header("Accept-Language", "en,fr;q=0.5").asString();
        assertThat(response.body()).isEqualTo("hello");
    }

    @Test
    public void testThatWeCanRetrieveMessagesIndividuallyUsingQueryParameter() throws Exception {
        HttpResponse<String> response = get("/i18n/welcome?locale=en")
                .header("Accept-Language", "fr")
                .asString();
        assertThat(response.body()).isEqualTo("hello");

        response = get("/i18n/welcome?locale=fr")
                .header("Accept-Language", "en")
                .asString();
        assertThat(response.body()).isEqualTo("bonjour");

        // Not supported locale, delegate to default
        response = get("/i18n/welcome?locale=fr-FR")
                .header("Accept-Language", "en").asString();
        assertThat(response.body()).isEqualTo("hello");

        response = get("/i18n/welcome?locale=fr_FR")
                .header("Accept-Language", "en").asString();
        assertThat(response.body()).isEqualTo("hello");
    }

    @Test
    public void testThatWeCanRetrieveAllMessagesUsingHeader() throws Exception {
        HttpResponse<String> response = get("/i18n").header("Accept-Language", "en").asString();
        assertThat(response.body()).contains("hello");

        response = get("/i18n").header("Accept-Language", "fr").asString();
        assertThat(response.body()).contains("bonjour");

        response = get("/i18n").header("Accept-Language", "fr-FR").asString();
        assertThat(response.body()).contains("hello");

        response = get("/i18n").header("Accept-Language", "fr_FR").asString();
        assertThat(response.body()).contains("hello");

        response = get("/i18n").header("Accept-Language", "fr,en;q=0.5").asString();
        assertThat(response.body()).contains("bonjour");

        response = get("/i18n").header("Accept-Language", "en,fr;q=0.5").asString();
        assertThat(response.body()).contains("hello");
    }

    @Test
    public void testThatWeCanRetrieveAllMessagesUsingQueryParameter() throws Exception {
        HttpResponse<String> response = get("/i18n?locale=en")
                .header("Accept-Language", "fr").asString();
        assertThat(response.body()).contains("hello");

        response = get("/i18n?locale=fr")
                .header("Accept-Language", "en").asString();
        assertThat(response.body()).contains("bonjour");

        response = get("/i18n?locale=fr-FR")
                .header("Accept-Language", "en").asString();
        assertThat(response.body()).contains("hello");

        response = get("/i18n?locale=fr_FR")
                .header("Accept-Language", "en").asString();
        assertThat(response.body()).contains("hello");
    }


}
