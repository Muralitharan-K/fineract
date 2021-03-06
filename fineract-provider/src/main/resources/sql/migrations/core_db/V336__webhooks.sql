--
-- Licensed to the Apache Software Foundation (ASF) under one
-- or more contributor license agreements. See the NOTICE file
-- distributed with this work for additional information
-- regarding copyright ownership. The ASF licenses this file
-- to you under the Apache License, Version 2.0 (the
-- "License"); you may not use this file except in compliance
-- with the License. You may obtain a copy of the License at
--
-- http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing,
-- software distributed under the License is distributed on an
-- "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
-- KIND, either express or implied. See the License for the
-- specific language governing permissions and limitations
-- under the License.
--

-- -----------------------------------------------------
-- INSERT `m_hook_templates`
-- -----------------------------------------------------

insert into m_hook_templates values(3, "Queue");

INSERT INTO `m_hook_schema` (`hook_template_id`, `field_type`, `field_name`, `optional`)
VALUES (3, 'string', 'Queue Name', 0);
INSERT INTO `m_hook_schema` (`hook_template_id`, `field_type`, `field_name`, `optional`)
VALUES (3, 'string', 'Queue Broker URL', 0);
INSERT INTO `m_hook_schema` (`hook_template_id`, `field_type`, `field_name`, `optional`)
VALUES (3, 'string', 'Username', 0);
INSERT INTO `m_hook_schema` (`hook_template_id`, `field_type`, `field_name`, `optional`)
VALUES (3, 'string', 'Password', 0);