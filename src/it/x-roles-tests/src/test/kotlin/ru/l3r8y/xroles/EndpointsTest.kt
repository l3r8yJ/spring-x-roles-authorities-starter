/*
 * MIT License
 *
 * Copyright (c) 2024 Ivan Ivanchuk
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ru.l3r8y.xroles

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EndpointsTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun `should pass opened endpoint`() {
        mvc.perform(get("/open")).andExpect(status().isOk)
    }

    @Test
    fun `should pass opened endpoint with X-Roles header`() {
        mvc.perform(
            get("/open").header("X-Roles", "fpoop")
        ).andExpect(status().isOk)
    }

    @Test
    @WithMockUser(username = "ruby")
    fun `should not pass closed endpoint without proper roles`() {
        mvc
            .perform(get("/closed"))
            .andExpect(status().`is`(HttpStatus.FORBIDDEN.value()))
    }

    @Test
    @WithMockUser(username = "ruby")
    fun `should pass closed endpoint with proper roles`() {
        mvc.perform(
            get("/closed").header("X-Roles", "dawg, monkey, cat")
        ).andExpect(status().isOk)
    }
}
